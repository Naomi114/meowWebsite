package tw.com.ispan.controller.pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.dto.pet.EditSearchDTO;
import tw.com.ispan.dto.pet.InputRescueCaseDto;
import tw.com.ispan.dto.pet.ModifyRescueCaseDto;
import tw.com.ispan.dto.pet.OutputRescueCaseDTO;
import tw.com.ispan.dto.pet.RescueCaseResponse;
import tw.com.ispan.dto.pet.RescueSearchCriteria;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.service.line.LineNotificationService;
import tw.com.ispan.service.pet.ImageService;
import tw.com.ispan.service.pet.RescueCaseService;

//此為救援案件crud
@CrossOrigin
@RestController
@RequestMapping(path = { "/api/RescueCase" })
public class RescueController {

	@Autowired
	private RescueCaseService rescueCaseService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RescueCaseRepository rescueCaseRepository;

	@Autowired
	private LineNotificationService lineNotificationService; // 引入 LINE 通知服務

	@Value("${back.domainName.url}")
	private String backDomainName;

	@Value("${file.petUpload.path}")
	private String petUploadPath;

	// 新增一筆救援案件----------------------------------------------------------------------------------------------------------------------
	@PostMapping(path = { "/add" })
	public RescueCaseResponse add(@RequestHeader("Authorization") String token,
			@RequestAttribute("memberId") Integer memberId, @Validated @RequestBody InputRescueCaseDto rescueCaseDto) {

		// 方法參數:
		// 1. 專案使用JWT(JSON Web Token)來管理會員登入，則可以從前端傳入的 JWT
		// 中提取重要資訊，且controller必須接收header中token字串
		// 2.
		// @RequestAttribute("memberId")為接收JsonWebTokenInterceptor類別中攔截近來此controller的request，解析token內攜帶的memberId
		// @RequestAttribute 的來源： @RequestAttribute
		// 用於提取由攔截器、過濾器或其他中間層處理時設置的屬性，而不是從請求參數中提取的值。
		// 3. rescueCaseDto傳進service存資料，而RescueCaseResponse回傳訊息給前端
		RescueCaseResponse response = new RescueCaseResponse();

		// 傳進來的資料需要驗證(前端即時驗證一次，後端驗證一次)
		// 1.驗證token(能通過JsonWebTokenInterceptor攔截器即驗證)並拿到會員id，需驗證此id有無在會員資料表中存在
		System.out.println("此為會員id" + memberId + "執行新增案件");
		if (memberId == null) {
			response.setSuccess(false);
			response.setMessage("必須給予會員id");
			return response;
		} else if (!memberRepository.existsById(memberId)) {
			response.setSuccess(false);
			response.setMessage("此會員id不存在於資料中");
			return response;
		}

		// 2.驗證必填資料、資料格式(沒寫傳進來dto接收會是預設初始值null或0)->加上@Validated於dto中直接進行驗證，如果驗證失敗，Spring
		// Boot會自動拋出錯誤

		// 3.前端傳圖片暫存url list，先將暫存資料夾中圖片移轉至永存資料夾，操作正確則回傳圖片新路徑，將新路徑存置資料庫中
		List<String> finalUrl = imageService.moveImages(rescueCaseDto.getCasePictures());
		System.out.println("圖片移動完畢!");
		List<CasePicture> casePictures = imageService.saveImage(finalUrl);
		// 需要設置檢查為如果圖片增添失敗==案件增添失敗!!!

		// 4. 新增案件至資料庫 先convertToEntity()轉為實體類別後，add()把該存的放進去(圖片、經緯度等..)再存入資料庫中
		RescueCase rescueCaseEntity = rescueCaseService.convertToEntity(rescueCaseDto, memberId);
		RescueCase rescueCase = rescueCaseService.add(rescueCaseEntity, casePictures);

		if (rescueCase != null) {
			// 新增成功
			response.setSuccess(true);
			response.setMessage("新增案件成功");
			return response;
		} else {
			// 新增失敗，如果rescueCase == null
			response.setSuccess(false);
			response.setMessage("新增案件失敗");
			return response;
		}
	}

	// 修改救援案件-----------------------------------------------------------------------------------------------------------------------------
	@PutMapping(path = { "/modify/{id}" })
	public RescueCaseResponse modifiedRescueCase(@PathVariable(name = "id") Integer caseId,
			@RequestHeader("Authorization") String token, @RequestAttribute("memberId") Integer memberId,
			@Validated @RequestBody ModifyRescueCaseDto dto) {

		// 除了原本新增案件的內容都可修改外，重點是多一個可修改caseState以及會傳imageIdandUrl進來，因此相較新增案件的這兩個屬性不是null
		// 案件id要從前端點選修改按鈕(按鈕做成超連結)時同時送出，因此id即藏在超連結送出的request line裡

		RescueCaseResponse response = new RescueCaseResponse();

		// 前端在看到某案件內頁面時，只有當此案件的memberId有對應上自己的，才會在前端看到「編輯此案件」的按鈕，才能進到可以按下此controller修改案就的按鈕
		// 1.驗證token(能通過JsonWebTokenInterceptor攔截器即驗證)並拿到會員id，需驗證此id有無在會員資料表中存在之餘，驗證此案件真的是這個會員po的，他才能修改
		System.out.println("此為會員id" + memberId + "執行修改案件");
		if (memberId == null) {
			response.setSuccess(false);
			response.setMessage("必須給予會員id");
			return response;
		} else if (!memberRepository.existsById(memberId)) {
			response.setSuccess(false);
			response.setMessage("此會員id不存在於資料中");
			return response;
		}

		// 2. 驗證此案件資料中的memberId真的有對應上此使用者的memberId
		// 如果不匹配
		if (!rescueCaseService.iCanModify(memberId, caseId)) {
			response.setSuccess(false);
			response.setMessage("此會員不可修改此案件");
			return response;
		}

		// 3.驗證必填資料、資料格式(沒寫傳進來dto接收會是預設初始值null或0)->加上@Validated於dto中直接進行驗證，如果驗證失敗，Spring
		// Boot會自動拋出錯誤

		// 4. 驗證此案件id是否存在於資料表中，有存在才繼續往service丟。
		if (caseId == null) {
			response.setSuccess(false);
			response.setMessage("必須給予案件id");
			return response;
		} else if (!rescueCaseService.exists(caseId)) {
			response.setSuccess(false);
			response.setMessage("id不存在於資料中");
			return response;
		}

		// 修改圖片
		// 先判斷getImageIdandUrl中有無不對應的部分(表示圖片被修改)，有修改的才需要被移到永存資料夾，同時修改圖片表中對應id的圖片url為新url
		// 返回對應的CasePicture實體，等等用來存進case物件中
		// List<CasePicture> newCasePictures =
		// imageService.saveModify(dto.getImageIdandUrl());
		// if (newCasePictures == null) {
		// response.setSuccess(false);
		// response.setMessage("圖片修改出問題");
		// return response;
		// }

		// 4. 驗證id存在，就去修改這筆資料，並且同時傳line message有追蹤該案件的會員(同時確定有追蹤商家line)
		RescueCase rescueCaseEntity = rescueCaseService.modifyConvertToEntity(dto);
		RescueCase updatedCase = rescueCaseService.modify(rescueCaseEntity, caseId, dto.getCasePictures());

		if (updatedCase != null) {
			// 修改成功
			response.setSuccess(true);
			response.setMessage("修改案件成功");
			return response;
		} else {
			// 修改失敗，回傳rescueCase == null
			response.setSuccess(false);
			response.setMessage("修改案件失敗");
			return response;
		}
	}

	// 刪除救援案件-----------------------------------------------------------------------------------------------------------------------------
	@DeleteMapping(path = { "/delete/{id}" })
	public RescueCaseResponse deleteRescueCase(@PathVariable("id") Integer id,
			@RequestHeader("Authorization") String token) {

		RescueCaseResponse response = new RescueCaseResponse();

		// 1.驗證token(能通過JsonWebTokenInterceptor攔截器即驗證)

		// 2. 驗證此id是否存在於資料表中，有存在才繼續往service丟去刪除
		if (id == null) {
			response.setSuccess(false);
			response.setMessage("必須給予案件id");
			return response;
		} else if (!rescueCaseService.exists(id)) {
			response.setSuccess(false);
			response.setMessage("id不存在於資料中");
			return response;
		}

		// 若id存在，就去修改這筆資料
		if (rescueCaseService.delete(id)) {
			response.setSuccess(true);
			response.setMessage("案件刪除成功");
			return response;
		} else {
			response.setSuccess(true);
			response.setMessage("案件刪除失敗");
			return response;
		}
	}

	// 查詢單筆救援案件(用戶點進去某case)-------------------------------------------------------------------------------------------------------------
	@GetMapping("/search/{id}")
	public OutputRescueCaseDTO searchRescueCase(@PathVariable("id") Integer caseId) {

		// 1. 非會員功能，不用驗證token

		OutputRescueCaseDTO rescueCase = rescueCaseService.searchRescueCase(caseId);
		if (rescueCase != null) {
			System.out.println(rescueCase.toString());
			// 返回前端，java物件會被springboot自動序列化為json格式
			return rescueCase;
		} else {
			System.out.println("此案件id不存在");
			return null;
		}
	}

	// 於編輯頁面中去查詢單筆救援案件(用戶編輯某case)
	// 因為查詢單筆案件會返回字串，但我希望返回id才能正確回填，因此多寫一個方法嗚嗚--------------------------------------
	@GetMapping("/editSearch/{id}")
	public EditSearchDTO editRescueCase(@PathVariable("id") Integer caseId) {

		// 1. 會員功能，需要驗證token(JwtConfig中)

		EditSearchDTO rescueCase = rescueCaseService.editSearchRescueCase(caseId);
		if (rescueCase != null) {
			System.out.println(rescueCase.toString());
			// 返回前端，java物件會被springboot自動序列化為json格式
			return rescueCase;
		} else {
			System.out.println("此案件id不存在");
			return null;
		}
	}

	// 根據條件查詢多筆救援案件(用戶使用條件搜尋欄)--------------------------------------------------------------------------------------------------------------
	@PostMapping("/search")
	public List<RescueCase> searchRescueCases(@RequestBody RescueSearchCriteria criteria,
			@RequestParam(defaultValue = "0") int page, // 前端沒丟參數就用預設值
			@RequestParam(defaultValue = "10") int size) {
		System.out.println("查詢條件" + criteria.toString());
		Pageable pageable = PageRequest.of(page, size);
		Page<RescueCase> resultPage = rescueCaseService.searchRescueCases(criteria, pageable);
		return resultPage.getContent();
	}

	// 分批(滾動加載)查詢所有救援案件(搜尋頁面展示所有案件)--------------------------------------------------------------------------------------------------------------
	@GetMapping("/search/allCases")
	public Map<String, Object> getAllCases(@RequestParam(defaultValue = "0") int offset, // 起始位置
			@RequestParam(defaultValue = "10") int limit, // 每次加載數量
			@RequestParam(defaultValue = "desc") String sortOrder // 排序條件：asc(舊到新) 或 desc(新到舊)
	) {
		List<OutputRescueCaseDTO> cases = rescueCaseService.getAllCases(offset, limit, sortOrder);

		// 返回數據和是否還有更多數據的標記
		Map<String, Object> response = new HashMap<>();
		response.put("cases", cases);
		response.put("hasMore", cases.size() == limit); // 如果等於，說明後端可能還有更多數據尚未返回

		return response;
	}

	// 依搜尋條件分批(滾動加載)查詢救援案件-------------------------------------------------------------------------
	@PostMapping("/search/infinite")
	public Map<String, Object> searchRescueCasesInfiniteScroll(
			@RequestBody RescueSearchCriteria criteria,
			@RequestParam(defaultValue = "0") int offset, // 起始位置
			@RequestParam(defaultValue = "10") int limit, // 每次加載數量
			@RequestParam(defaultValue = "desc") String sortOrder // 排序條件
	) {
		// 呼叫 service 層的方法來獲取數據
		List<OutputRescueCaseDTO> cases = rescueCaseService.searchRescueCasesInfinite(criteria, offset, limit,
				sortOrder);

		// 建立回傳格式
		Map<String, Object> response = new HashMap<>();
		response.put("cases", cases);
		response.put("hasMore", cases.size() == limit); // 是否還有更多數據

		return response;
	}

	// 返回某類型全部案件座標給前端google地圖使用(要幫另外兩種案件也加上這個)
	@GetMapping("/getLocations")
	public List<Map<String, Object>> getRescueCasesLocations() {
		List<RescueCase> cases = rescueCaseService.getAllCases();

		List<Map<String, Object>> response = new ArrayList<>();
		for (RescueCase rescueCase : cases) {
			Map<String, Object> caseData = new HashMap<>();
			caseData.put("caseTitle", rescueCase.getCaseTitle());
			caseData.put("latitude", rescueCase.getLatitude());
			caseData.put("longitude", rescueCase.getLongitude());
			caseData.put("rescueReason", rescueCase.getRescueReason());
			caseData.put("publicationTime", rescueCase.getPublicationTime());
			caseData.put("city", rescueCase.getCity().getCity());
			caseData.put("district", rescueCase.getDistrictArea().getDistrictAreaName());
			caseData.put("caseState", rescueCase.getCaseState());
			caseData.put("caseId", rescueCase.getRescueCaseId());
			caseData.put("caseType", "rescueCase");

			// 修正 casePictures 中的 pictureUrl，確保前端可以訪問
			List<Map<String, String>> fixedCasePictures = new ArrayList<>();
			for (CasePicture picture : rescueCase.getCasePictures()) {
				Map<String, String> pictureData = new HashMap<>();
				String originalPath = picture.getPictureUrl(); // 取得原始路徑
				String fixedPath = originalPath.replace(petUploadPath, backDomainName + "/upload/"); // 替換成可訪問 URL
				pictureData.put("pictureUrl", fixedPath);
				fixedCasePictures.add(pictureData);
			}

			caseData.put("casePictures", fixedCasePictures); // 更新處理後的圖片路徑

			response.add(caseData);
		}

		return response;
	}

	// 帶條件的返回案件座標資訊給前端google地圖使用(要幫另外兩種案件也加上這個)
	@GetMapping("/getLocations/filters")
	public ResponseEntity<List<Map<String, Object>>> getFilteredCases(
			@RequestParam(required = false) List<Integer> caseState, @RequestParam(required = false) Integer city,
			@RequestParam(required = false) Integer district, @RequestParam(required = false) List<Integer> species,
			@RequestParam(required = false) Integer breedId, @RequestParam(required = false) List<Integer> furColors,
			@RequestParam(required = false) Boolean suspLost,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

		// 調用 Service 層獲取篩選結果
		List<Map<String, Object>> cases = rescueCaseService.getFilteredCases(caseState, city, district, species,
				breedId, furColors, suspLost, startDate, endDate);

		//

		return ResponseEntity.ok(cases);
	}

	// 管理員分析案件數據，搭配char.js
	@GetMapping("/analysis")
	public Map<String, Object> getCaseStats() {
		Map<String, Object> stats = new HashMap<>();

		// **前10名最多瀏覽/追蹤的案件**
		List<RescueCase> topCases = rescueCaseRepository.findTop10ByOrderByViewCountDesc();
		List<Map<String, Object>> caseData = topCases.stream()
				.map(c -> {
					Map<String, Object> map = new HashMap<>();
					map.put("caseTitle", c.getCaseTitle());
					map.put("viewCount", c.getViewCount());
					map.put("follow", c.getFollow());
					map.put("rescueCaseId", c.getRescueCaseId());
					return map;
				})
				.collect(Collectors.toList());
		stats.put("topCases", caseData);

		// 最後10名瀏覽/追蹤的案件
		List<RescueCase> bottomCases = rescueCaseRepository.findTop10ByOrderByViewCountAsc();
		List<Map<String, Object>> bottomCaseData = bottomCases.stream()
				.map(c -> {
					Map<String, Object> map = new HashMap<>();
					map.put("caseTitle", c.getCaseTitle());
					map.put("viewCount", c.getViewCount());
					map.put("follow", c.getFollow());
					map.put("rescueCaseId", c.getRescueCaseId());
					return map;
				})
				.collect(Collectors.toList());
		stats.put("bottomCases", bottomCaseData);

		// 各縣市案件數量
		List<Object[]> cityCases = rescueCaseRepository.countCasesByCity();
		List<Map<String, Object>> cityData = cityCases.stream()
				.map(c -> {
					Map<String, Object> map = new HashMap<>();
					map.put("city", c[0]);
					map.put("count", c[1]);
					return map;
				})
				.collect(Collectors.toList()); // ✅ Java 8 兼容寫法
		stats.put("caseByCity", cityData);

		// **狗 vs 貓案件數量**
		long dogCases = rescueCaseRepository.countBySpecies_Species("狗");
		long catCases = rescueCaseRepository.countBySpecies_Species("貓");

		Map<String, Object> speciesCount = new HashMap<>();
		speciesCount.put("dog", dogCases);
		speciesCount.put("cat", catCases);
		stats.put("speciesCount", speciesCount);

		return stats;
	}

	// 用於返回某會員所屬的救援案件(會員中心使用)
	@GetMapping("/memberRescueCases")
	public ResponseEntity<List<Map<String, Object>>> getMemberRescueCases(
			@RequestHeader("Authorization") String token,
			@RequestAttribute("memberId") Integer memberId) {

		if (memberId == null) {
			return ResponseEntity.badRequest().build();
		}

		List<RescueCase> rescueCases = rescueCaseRepository.findByMemberId(memberId);

		List<Map<String, Object>> response = rescueCases.stream().map(caseItem -> {
			Map<String, Object> caseMap = new HashMap<>();
			caseMap.put("rescueCaseId", caseItem.getRescueCaseId());
			caseMap.put("caseTitle", caseItem.getCaseTitle());
			caseMap.put("caseState", caseItem.getCaseState().getCaseStatement());
			caseMap.put("lastUpdateTime", caseItem.getLastUpdateTime());
			caseMap.put("publicationTime", caseItem.getPublicationTime());
			return caseMap;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}
}
