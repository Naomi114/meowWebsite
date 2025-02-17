package tw.com.ispan.service.pet;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.domain.pet.forRescue.CanAfford;
import tw.com.ispan.domain.pet.forRescue.RescueDemand;
import tw.com.ispan.dto.pet.EditSearchDTO;
import tw.com.ispan.dto.pet.InputRescueCaseDto;
import tw.com.ispan.dto.pet.ModifyRescueCaseDto;
import tw.com.ispan.dto.pet.OutputRescueCaseDTO;
import tw.com.ispan.dto.pet.RescueSearchCriteria;
import tw.com.ispan.jwt.JsonWebTokenUtility;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistrictAreaRepository;
import tw.com.ispan.repository.pet.FollowRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.forRescue.CanAffordRepository;
import tw.com.ispan.repository.pet.forRescue.RescueDemandRepository;
import tw.com.ispan.service.line.LineNotificationService;
import tw.com.ispan.specification.RescueCaseSpecification;
//import tw.com.ispan.service.JwtService;
import tw.com.ispan.util.LatLng;

@Service
@Transactional
public class RescueCaseService {

	@Value("${back.domainName.url}") // http://localhost:8080
	private String domainName;

	@Value("${front.domainName.url}") // http://localhost:5173
	private String frontDomainName;

	@Value("${file.final-upload-dir}") // 圖片儲存於後端的路徑
	private String finalUploadDir;

	@Value("${file.petUpload.path}")
	private String petUploadPath;

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RescueCaseRepository rescueCaseRepository;
	@Autowired
	private SpeciesRepository speciesRepository;
	@Autowired
	private BreedRepository breedRepository;
	@Autowired
	private FurColorRepository furColorRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private DistrictAreaRepository districtAreaRepository;
	@Autowired
	private RescueDemandRepository rescueDemandRepository;
	@Autowired
	private CanAffordRepository canAffordRepository;
	@Autowired
	private CaseStateRepository caseStateRepository;
	@Autowired
	private JsonWebTokenUtility jsonWebTokenUtility;
	@Autowired
	private LineNotificationService lineNotificationService; // 引入 LINE 通知服務
	@Autowired
	private GeocodingService geocodingService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private FollowRepository followRepository;

	// 新增案件:手動將傳進來的dto轉回entity，才能丟進jpa增刪修方法
	public RescueCase convertToEntity(InputRescueCaseDto dto, Integer memberId) {

		RescueCase rescueCase = new RescueCase();

		// 沒有對應資料表的屬性直接塞
		rescueCase.setCaseTitle(dto.getCaseTitle());
		rescueCase.setGender(dto.getGender());
		rescueCase.setSterilization(dto.getSterilization());
		rescueCase.setAge(dto.getAge());
		rescueCase.setMicroChipNumber(dto.getMicroChipNumber());
		rescueCase.setSuspLost(dto.getSuspLost());
		rescueCase.setStreet(dto.getStreet());
		rescueCase.setRescueReason(dto.getRescueReason());
		rescueCase.setTag(dto.getTag());

		// 以下傳id進來，找到對應資料再塞回enitity物件中
		// 會員
		Optional<Member> result0 = memberRepository.findById(memberId);
		if (result0 != null && result0.isPresent()) {
			rescueCase.setMember(result0.get()); //
		}

		// 物種
		Optional<Species> result1 = speciesRepository.findById(dto.getSpeciesId());
		if (result1 != null && result1.isPresent()) {
			rescueCase.setSpecies(result1.get()); // 是存一個Species物件在內，result1.get()是此物件的地址!
													// 要印出對應的物種必須result1.get().getSpecies()
		}

		// 品種
		Optional<Breed> result2 = breedRepository.findById(dto.getBreedId());
		if (result2 != null && result2.isPresent()) {
			rescueCase.setBreed(result2.get());
		}

		// 毛色
		Optional<FurColor> result3 = furColorRepository.findById(dto.getFurColorId());
		if (result3 != null && result3.isPresent()) {
			rescueCase.setFurColor(result3.get());
		}

		// city
		Optional<City> result4 = cityRepository.findById(dto.getCityId());
		if (result4 != null && result4.isPresent()) {
			rescueCase.setCity(result4.get());
		}

		// districtArea
		Optional<DistrictArea> result5 = districtAreaRepository.findById(dto.getDistrictAreaId());
		if (result5 != null && result5.isPresent()) {
			rescueCase.setDistrictArea(result5.get());
		}

		// rescueDemands
		List<RescueDemand> rescueDemands = rescueDemandRepository.findAllById(dto.getRescueDemands());
		rescueCase.setRescueDemands(rescueDemands);

		// canAffords
		List<CanAfford> canAffords = canAffordRepository.findAllById(dto.getCanAffords());
		rescueCase.setCanAffords(canAffords);

		// caseState
		// 新增案件時dto內不會含caseState資料，而是等這個rescueCase被save()會自動觸發初始化程式塞入預設值待救援
		// 修改案件時dto內會有caseState資料，因此要塞到rescueCase物件中
		if (dto.getCaseStateId() != null) {
			Optional<CaseState> result6 = caseStateRepository.findById(dto.getCaseStateId());
			if (result6 != null && result6.isPresent()) {
				rescueCase.setCaseState(result6.get());
			}
		}

		return rescueCase;
	}

	// 增加非使用者填寫資料並insert新增一筆案件到資料庫
	public RescueCase add(RescueCase rescueCase, List<CasePicture> casePicture) {

		// 案件id資料庫中自動生成
		// 最後把關確保用戶沒有手動填的member、latitude、longitude、publicationTime、lastUpadteTime、caseStateId、等必填資料塞進來，才能存進資料庫中

		// 設置經緯度
		String adress = rescueCase.getCity().getCity() + rescueCase.getDistrictArea().getDistrictAreaName()
				+ rescueCase.getStreet();
		System.out.println(adress);
		try {
			LatLng latLng = geocodingService.getCoordinatesFromAddress(adress);
			if (latLng != null) {
				rescueCase.setLatitude(latLng.getLat());
				rescueCase.setLongitude(latLng.getLng());
			}
		} catch (JsonProcessingException e) {
			System.out.println("請求座標API失敗");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("不支援編碼");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.out.println("編碼格式錯誤");
			e.printStackTrace();
		}

		// 設置圖片關聯(此時圖片資料庫已有資料)，利用controller傳進來的CasePicture實體設置
		rescueCase.setCasePictures(casePicture);

		// 設置預設caseState(待救援id為3，用3去把物件查出來再塞進去，因為主實體rescueCae在save()時裏頭的關聯屬性的值都只能是永續狀態)
		Optional<CaseState> result = caseStateRepository.findById(3);
		if (result != null && result.isPresent()) {
			rescueCase.setCaseState(result.get());
		}

		// 存進資料庫中
		if (rescueCaseRepository.save(rescueCase) != null) {
			System.out.println("新增成功");
			return rescueCase;
		}
		System.out.println("新增失敗");
		return null;

	}

	// 修改案件專用的dto轉換(和新增案件不同在於casePicture資料型態)
	public RescueCase modifyConvertToEntity(ModifyRescueCaseDto dto) {

		RescueCase rescueCase = new RescueCase();

		// 沒有對應資料表的屬性直接塞(不比對了不管有沒有改過都執行，懶的寫條件)
		rescueCase.setCaseTitle(dto.getCaseTitle());
		rescueCase.setGender(dto.getGender());
		rescueCase.setSterilization(dto.getSterilization());
		rescueCase.setAge(dto.getAge());
		rescueCase.setMicroChipNumber(dto.getMicroChipNumber());
		rescueCase.setSuspLost(dto.getSuspLost());
		rescueCase.setStreet(dto.getStreet());
		rescueCase.setRescueReason(dto.getRescueReason());
		rescueCase.setTag(dto.getTag());

		// 以下傳id進來，找到對應資料再塞回enitity物件中
		// 物種
		Optional<Species> result1 = speciesRepository.findById(dto.getSpeciesId());
		if (result1 != null && result1.isPresent()) {
			rescueCase.setSpecies(result1.get());
		}

		// 品種
		Optional<Breed> result2 = breedRepository.findById(dto.getBreedId());
		if (result2 != null && result2.isPresent()) {
			rescueCase.setBreed(result2.get());
		}

		// 毛色
		Optional<FurColor> result3 = furColorRepository.findById(dto.getFurColorId());
		if (result3 != null && result3.isPresent()) {
			rescueCase.setFurColor(result3.get());
		}

		// city
		Optional<City> result4 = cityRepository.findById(dto.getCityId());
		if (result4 != null && result4.isPresent()) {
			rescueCase.setCity(result4.get());
		}

		// districtArea
		Optional<DistrictArea> result5 = districtAreaRepository.findById(dto.getDistrictAreaId());
		if (result5 != null && result5.isPresent()) {
			rescueCase.setDistrictArea(result5.get());
		}

		// rescueDemands
		List<RescueDemand> rescueDemands = rescueDemandRepository.findAllById(dto.getRescueDemands());
		rescueCase.setRescueDemands(rescueDemands);

		// canAffords
		List<CanAfford> canAffords = canAffordRepository.findAllById(dto.getCanAffords());
		rescueCase.setCanAffords(canAffords);

		// caseState
		// 新增案件時dto內不會含caseState資料，而是等這個rescueCase被save()會自動觸發初始化程式塞入預設值待救援
		// 修改案件時dto內會有caseState資料，因此要塞到rescueCase物件中
		if (dto.getCaseStateId() != null) {
			Optional<CaseState> result6 = caseStateRepository.findById(dto.getCaseStateId());
			if (result6 != null && result6.isPresent()) {
				rescueCase.setCaseState(result6.get());
			}
		}

		return rescueCase;
	}

	// 修改案件----------------------------------------------------------------------------------------------
	public RescueCase modify(RescueCase rescueCase, Integer caseId, List<Map<String, String>> casePictures) {

		// 必須拿這個新物件有的資料去修改舊物件，這樣才能留存經緯度、創建時間等資訊，而不是用新物件直接save()這些資訊會空掉，最後存修改後的舊物件
		Optional<RescueCase> result = rescueCaseRepository.findById(caseId);
		if (result != null && result.isPresent()) {

			RescueCase old = result.get();

			// 決定是否通知會員的新舊變更比對，確保是「重要的變更」才發通知
			// 包含標題、物種、品種、毛色、性別、絕育狀況、年齡、晶片號碼、是否懷疑遺失、救援原因、地址、
			// 使用Objects.equals(a, b) 會自動處理 null，避免 NullPointerException
			boolean isSignificantUpdate = !Objects.equals(old.getCaseTitle(), rescueCase.getCaseTitle()) ||
					!Objects.equals(old.getSpecies(), rescueCase.getSpecies()) ||
					!Objects.equals(old.getBreed(), rescueCase.getBreed()) ||
					!Objects.equals(old.getFurColor(), rescueCase.getFurColor()) ||
					!Objects.equals(old.getGender(), rescueCase.getGender()) ||
					!Objects.equals(old.getSterilization(), rescueCase.getSterilization()) ||
					!Objects.equals(old.getAge(), rescueCase.getAge()) ||
					!Objects.equals(old.getMicroChipNumber(), rescueCase.getMicroChipNumber()) ||
					!Objects.equals(old.getSuspLost(), rescueCase.getSuspLost()) ||
					!Objects.equals(old.getDistrictArea(), rescueCase.getDistrictArea()) ||
					!Objects.equals(old.getCity(), rescueCase.getCity()) ||
					!Objects.equals(old.getStreet(), rescueCase.getStreet()) ||
					!Objects.equals(old.getCaseState(), rescueCase.getCaseState()) ||
					!Objects.equals(old.getRescueReason(), rescueCase.getRescueReason());

			// 更新案件資訊:
			// 舊物件一定有的，且不會被會員改寫的有member、publicationTime、lastUpadteTime，不用去動(更新時間會受到domain
			// jpa註解自動改變)
			if (rescueCase.getCaseTitle() != null) {
				old.setCaseTitle(rescueCase.getCaseTitle());
			}
			if (rescueCase.getSpecies() != null) {
				old.setSpecies(rescueCase.getSpecies());
			}
			if (rescueCase.getBreed() != null) {
				old.setBreed(rescueCase.getBreed());
			}
			if (rescueCase.getFurColor() != null) {
				old.setFurColor(rescueCase.getFurColor());
			}
			if (rescueCase.getGender() != null) {
				old.setGender(rescueCase.getGender());
			}
			if (rescueCase.getSterilization() != null) {
				old.setSterilization(rescueCase.getSterilization());
			}
			if (rescueCase.getAge() != null) {
				old.setAge(rescueCase.getAge());
			}
			if (rescueCase.getMicroChipNumber() != null) {
				old.setMicroChipNumber(rescueCase.getMicroChipNumber());
			}
			if (rescueCase.getSuspLost() != null) {
				old.setSuspLost(rescueCase.getSuspLost());
			}
			if (rescueCase.getDistrictArea() != null) {
				old.setDistrictArea(rescueCase.getDistrictArea());
			}
			if (rescueCase.getCity() != null) {
				old.setCity(rescueCase.getCity());
			}
			if (rescueCase.getStreet() != null) {
				old.setStreet(rescueCase.getStreet());
			}
			if (rescueCase.getRescueReason() != null) {
				old.setRescueReason(rescueCase.getRescueReason());
			}
			if (rescueCase.getCaseState() != null) {
				old.setCaseState(rescueCase.getCaseState());
			}
			// 更新圖片
			List<CasePicture> updatedCasePictures = imageService.updateCasePictures(old.getCasePictures(),
					casePictures);
			old.getCasePictures().clear(); // 先清除原有的內容，保留 Hibernate 追蹤
			old.getCasePictures().addAll(updatedCasePictures); // 再加入新的圖片

			if (rescueCase.getRescueDemands() != null) {
				old.setRescueDemands(rescueCase.getRescueDemands());
			}
			if (rescueCase.getCanAffords() != null) {
				old.setCanAffords(rescueCase.getCanAffords());
			}
			if (rescueCase.getTag() != null) {
				old.setTag(rescueCase.getTag());
			}

			// 如果地址有更新到則經緯度要重新抓
			// 設置經緯度
			String adress = rescueCase.getCity().getCity() + rescueCase.getDistrictArea().getDistrictAreaName()
					+ rescueCase.getStreet();
			try {
				LatLng latLng = geocodingService.getCoordinatesFromAddress(adress);
				if (latLng != null) {
					old.setLatitude(latLng.getLat());
					old.setLongitude(latLng.getLng());
				}
			} catch (JsonProcessingException e) {
				System.out.println("請求座標API失敗");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				System.out.println("不支持的編碼");
				e.printStackTrace();
			} catch (URISyntaxException e) {
				System.out.println("URI 格式無效");
				e.printStackTrace();
			}

			// 修改完後，將含有新資料的舊物件存回去
			RescueCase savedcase = rescueCaseRepository.save(old);
			if (savedcase != null) {
				// **如果是重要變更，則發送 LINE 通知**
				if (isSignificantUpdate) {
					notifyFollowers(old);
				}
				System.out.println("案件修改成功");
				return savedcase;
			} else {
				System.out.println("案件修改失敗");
				return null;
			}
		} else {
			// 表此id不存在於案件表中，但controller已經驗證過存在才會進來service，理論上跑不到這條
			return null;
		}
	}

	// **發送 LINE 通知給追蹤者**
	private void notifyFollowers(RescueCase rescueCase) {

		System.out.println(rescueCase + "有發生變更，要傳送通知了!!!");

		// 找尋追蹤表中有追蹤此案件的會員
		List<Integer> memberIds = followRepository.findMemberIdsByRescueCaseId(rescueCase.getRescueCaseId());

		System.out.println("要發送給會員ID" + memberIds.toString());

		// 可以訪問前端案件頁面的網址(因為這裡是rescueCase Service因此只會是編輯救援案件才會用到)
		String caseUrl = frontDomainName + "/pet/rescueCase/" + rescueCase.getRescueCaseId();

		for (Integer memberId : memberIds) {
			Member member = memberRepository.findById(memberId).orElse(null);
			// 如果此會員id存在+有用line登入+有開啟平台line追蹤
			if (member != null && member.getLineId() != null && member.isFollowed()) {
				lineNotificationService.sendFlexNotification(member.getLineId(),
						"案件變更通知",
						"你追蹤的案件「" + rescueCase.getCaseTitle() + "」已更新，請查看詳情。",
						caseUrl);
			}
		}
	}

	// 刪除案件------------------------------------------------------------------------------------------
	public boolean delete(Integer id) {
		if (id != null && rescueCaseRepository.existsById(id)) {
			rescueCaseRepository.deleteById(id); // 沒有返回值
			return true;
		}
		return false;
	}

	// 查詢一筆案件(用於抓單個案件資訊)
	public OutputRescueCaseDTO searchRescueCase(Integer caseId) {
		Optional<RescueCase> result = rescueCaseRepository.findById(caseId);
		// 如果要修改圖片路徑，就必須額外用DTO來返回資料給前端，避免修改到原始資料庫casePicture表資料
		if (result.isPresent()) {
			RescueCase rescueCase = result.get();

			// 使用定義好的建構子直接創建 DTO 並複製 RescueCase 的數據
			OutputRescueCaseDTO rescueCaseDTO = new OutputRescueCaseDTO(rescueCase);

			// 將memberId也存進去
			rescueCaseDTO.setMemberId(rescueCase.getMember().getMemberId());

			// 轉換圖片路徑並存入 Map
			List<Map<String, String>> updatedPictureUrls = rescueCase.getCasePictures().stream().map(picture -> {
				Map<String, String> pictureMap = new HashMap<>();
				pictureMap.put("pictureUrl",
						picture.getPictureUrl().replace(petUploadPath + "final/", domainName + "/upload/final/"));
				return pictureMap;
			}).toList();

			// 設置 DTO 中的 casePictures
			rescueCaseDTO.setCasePictures(updatedPictureUrls);

			return rescueCaseDTO;
		}
		return null;
	}

	// 用戶於編輯案件頁面回填一筆案件資料(用於抓單個案件資訊)
	public EditSearchDTO editSearchRescueCase(Integer caseId) {
		Optional<RescueCase> result = rescueCaseRepository.findById(caseId);
		// 如果要修改圖片路徑，就必須額外用DTO來返回資料給前端，避免修改到原始資料庫casePicture表資料
		if (result.isPresent()) {
			RescueCase rescueCase = result.get();

			// 使用定義好的建構子直接創建 DTO 並複製 RescueCase 的數據
			EditSearchDTO rescueCaseDTO = new EditSearchDTO(rescueCase);

			// 將memberId也存進去
			rescueCaseDTO.setMemberId(rescueCase.getMember().getMemberId());

			// 轉換圖片數據，確保 casePictureId 也包含在內，並返回統一格式
			List<Map<String, String>> updatedPictureUrls = rescueCase.getCasePictures().stream().map(picture -> {
				Map<String, String> pictureMap = new HashMap<>();
				pictureMap.put("casePictureId", String.valueOf(picture.getCasePictureId())); // 轉換成字串
				pictureMap.put("pictureUrl",
						picture.getPictureUrl().replace(petUploadPath + "final/",
								domainName + "/upload/final/"));
				return pictureMap;
			}).collect(Collectors.toList());

			// 設置 DTO 的 casePictures
			rescueCaseDTO.setCasePictures(updatedPictureUrls);

			return rescueCaseDTO;
		}
		return null;
	}

	// 模糊查詢案件(根據用戶查詢條件和分頁請求返回查詢結果List<RescueCase>)-----------------------------------------------------------------------------------------
	public Page<RescueCase> searchRescueCases(RescueSearchCriteria criteria, Pageable pageable) {
		return rescueCaseRepository.findAll(RescueCaseSpecification.withRescueSearchCriteria(criteria), pageable);
	}

	// 分批查詢所有案件--------------------------------------------------------------------------------------
	public List<OutputRescueCaseDTO> getAllCases(int offset, int limit, String sortOrder) {
		// 動態排序：根據 sortOrder 設置排序方向(新到舊/舊到新)
		Sort sort = "desc".equalsIgnoreCase(sortOrder) ? Sort.by("lastUpdateTime").descending()
				: Sort.by("lastUpdateTime").ascending();

		// 設定分頁
		Pageable pageable = PageRequest.of(offset / limit, limit, sort);

		List<RescueCase> cases = rescueCaseRepository.findAllCases(pageable);
		// 轉換成 DTO(並塞入圖片url)
		return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// 依條件分批查詢所有案件(滾動加載)------------------------------------------------------------------------------------
	public List<OutputRescueCaseDTO> searchRescueCasesInfinite(RescueSearchCriteria criteria, int offset, int limit,
			String sortOrder) {
		// 設定排序方式
		Sort sort = "desc".equalsIgnoreCase(sortOrder) ? Sort.by("lastUpdateTime").descending()
				: Sort.by("lastUpdateTime").ascending();

		// 設定分頁
		Pageable pageable = PageRequest.of(offset / limit, limit, sort);

		// 透過 Specification 查詢符合條件的案件
		Page<RescueCase> casesPage = rescueCaseRepository
				.findAll(RescueCaseSpecification.withRescueSearchCriteria(criteria), pageable);

		// 將查詢結果轉換為 DTO，並返回
		return casesPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// 分頁查詢所有案件-塞入圖片!!----------------------------------------------------------------------------------------
	private OutputRescueCaseDTO convertToDTO(RescueCase rescueCase) {
		OutputRescueCaseDTO dto = new OutputRescueCaseDTO(rescueCase);

		// 提取圖片 URL，將本地路徑轉換成前端可訪問的 HTTP URL
		if (rescueCase.getCasePictures() != null) {
			List<Map<String, String>> pictureUrls = rescueCase.getCasePictures().stream().map(pic -> {
				String filePath = pic.getPictureUrl();

				// 確保檔案路徑是相對的（適用於 Windows 和 Linux）
				filePath = filePath.replace("\\", "/"); // 確保所有路徑符號為 "/"
				if (filePath.startsWith(finalUploadDir.replace("\\", "/"))) {
					filePath = filePath.substring(finalUploadDir.length());
				}

				// 組合成完整的 HTTP URL
				String imageUrl = domainName + "/upload/final/pet/images/" + filePath;

				// 返回物件（符合前端需求格式）
				Map<String, String> imageMap = new HashMap<>();
				imageMap.put("pictureUrl", imageUrl);
				return imageMap;
			}).collect(Collectors.toList());

			dto.setCasePictures(pictureUrls);
		}
		return dto;
	}

	// 不分頁查詢所有案件(給googlemap使用)
	public List<RescueCase> getAllCases() {
		return rescueCaseRepository.findAll();
	}

	// 確認案件是否存在於資料庫中-------------------------------------------------------------------------------------------
	public boolean exists(Integer id) {
		if (id != null) {
			return rescueCaseRepository.existsById(id);
		}
		return false;
	}

	// 驗證會員與案件中會員id是否匹配，匹配回傳true表示能修改
	public boolean iCanModify(Integer memberId, Integer caseId) {
		Optional<RescueCase> result = rescueCaseRepository.findById(caseId);
		if (result != null && result.isPresent()) {
			RescueCase Case = result.get();

			if (Case.getMember().getMemberId() == memberId) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// 返回給google地圖經條件篩選的救援案件
	public List<Map<String, Object>> getFilteredCases(List<Integer> caseState, Integer city, Integer district,
			List<Integer> species, Integer breedId, List<Integer> furColors, Boolean suspLost, LocalDate startDate,
			LocalDate endDate) {

		// 查詢資料庫取得符合條件的案件
		List<RescueCase> filteredCases = rescueCaseRepository.findCasesWithFilters(caseState, city, district, species,
				breedId, furColors, suspLost, startDate, endDate);

		System.out.println("查到的案件" + filteredCases);

		// 轉換成前端需要的格式
		List<Map<String, Object>> response = new ArrayList<>();
		for (RescueCase caseItem : filteredCases) {
			Map<String, Object> caseData = new HashMap<>();
			caseData.put("caseTitle", caseItem.getCaseTitle());
			caseData.put("latitude", caseItem.getLatitude());
			caseData.put("longitude", caseItem.getLongitude());
			caseData.put("rescueReason", caseItem.getRescueReason());
			caseData.put("publicationTime", caseItem.getPublicationTime());
			caseData.put("city", caseItem.getCity().getCity());
			caseData.put("district", caseItem.getDistrictArea().getDistrictAreaName());
			caseData.put("caseState", caseItem.getCaseState());
			caseData.put("caseId", caseItem.getRescueCaseId());
			caseData.put("caseType", "rescueCase");

			// 轉換圖片 URL（從本地端轉換為可訪問的 URL）
			List<Map<String, String>> pictureUrls = caseItem.getCasePictures().stream().map(picture -> {
				// 取得純檔名
				String fileName = Paths.get(picture.getPictureUrl()).getFileName().toString();
				// 返回一個 Map 物件
				return Map.of("pictureUrl", domainName + "/upload/final/pet/images/" + fileName);
			}).collect(Collectors.toList());

			caseData.put("casePictures", pictureUrls);

			response.add(caseData);
		}

		return response;
	}

}
