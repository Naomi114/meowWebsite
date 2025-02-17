package tw.com.ispan.controller.pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.dto.pet.RescueCaseResponse;
import tw.com.ispan.jwt.JsonWebTokenUtility;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.service.pet.FollowService;

//此為會員追蹤某案件
@RestController
@RequestMapping(path = { "/api/Case/follow" })
public class FollowController {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	JsonWebTokenUtility jsonWebTokenUtility; // 用來解析不用驗證token的方法，裡面的memberId

	@Autowired
	private FollowService followService;

	// 會員追蹤或取消追蹤某案件(重複按就會追蹤或取消追蹤)
	// 當按下follow要能區分使用者是對rescue, lost還是adoption
	// case執行追蹤，因為相同caseId在這三表中都有，要查對表。caseType數據會藏在前端的按鈕中
	@PutMapping("/add")
	public RescueCaseResponse followCase(@RequestHeader("Authorization") String token,
			@RequestAttribute("memberId") Integer memberId, @RequestParam Integer caseId,
			@RequestParam String caseType) {

		// 此response其實不限至於rescueCase使用，但為我自己開發時創立的，為避免和其他人設置的衝突所以取狹義名稱
		RescueCaseResponse response = new RescueCaseResponse();

		// 1.驗證token(能通過JsonWebTokenInterceptor攔截器即驗證)並拿到會員id，需驗證此id有無在會員資料表中存在
		System.out.println("此為會員id" + memberId + "執行案件追蹤");
		if (memberId == null) {
			response.setSuccess(false);
			response.setMessage("必須給予會員id");
			return response;
		} else if (!memberRepository.existsById(memberId)) {
			response.setSuccess(false);
			response.setMessage("此會員id不存在於資料中");
			return response;
		}

		// 2. 驗證前端有傳遞caseId和一併傳遞案件類型過來，且此caseType案件表中此id案件存在
		if (caseId == null || caseType == null || caseType.isEmpty()) {
			response.setSuccess(false);
			response.setMessage("必須給予案件id和案件類型");
			return response;
		} else if (!followService.caseExists(caseId, caseType)) {
			response.setSuccess(false);
			response.setMessage(caseType + "表中不存在id為" + caseId + "的案件");
			return response;
		}

		// 3. 防重複追蹤： 在follow表新增之前，應該檢查是否該會員已經追蹤過此案件
		if (followService.checkIfFollowExists(memberId, caseId, caseType)) {
			// 如果已經存在了，重複按表示要取消追蹤(從follow表刪除) 正常設計可能不會刪除資料是改為隱藏甚至紀錄反覆追蹤的時間，但我懶得設計這麼複雜
			followService.removeFollow(memberId, caseId, caseType);
			response.setSuccess(true);
			response.setMessage("會員id" + memberId + "的會員取消追蹤" + caseType + caseId + "案件");
			return response;
		}

		// 4. 確認沒追蹤過後，才增添follow表資料
		followService.addFollow(memberId, caseId, caseType);

		// 5. 更新對應案件表中的follow欄位(總追蹤數)
		int newFollowCount = followService.updateFollowCount(caseId, caseType);

		response.setSuccess(true);
		response.setCount(newFollowCount); // 返回被追蹤的案件目前總追蹤數
		response.setMessage("會員id" + memberId + "的會員成功追蹤" + caseType + caseId + "案件");
		return response;

	}

	// 前端用於判斷該用戶有無追蹤此案件，決定顯示已追蹤或追蹤
	@GetMapping("/status")
	public ResponseEntity<Map<String, Boolean>> checkFollowStatus(
			@RequestHeader("Authorization") String token,
			@RequestAttribute("memberId") Integer memberId,
			@RequestParam Integer caseId,
			@RequestParam String caseType) {

		// 這個請求只需要用在已登入的會員(才需要判斷有沒有追蹤過)
		// 沒登入的會員不管怎麼樣都顯示追蹤，但如果想按會先跳到登入

		System.out.println("會員 ID：" + memberId);
		Map<String, Boolean> response = new HashMap<>();
		boolean isFollowing = followService.checkIfFollowExists(memberId, caseId, caseType);
		response.put("isFollowing", isFollowing);

		return ResponseEntity.ok(response);
	}

	// 用於返回某會員有追蹤的案件列表給前端(會員中心)
	@GetMapping("/list")
	public ResponseEntity<List<Map<String, Object>>> listFollowedCases(
			@RequestHeader("Authorization") String token,
			@RequestAttribute("memberId") Integer memberId) {

		if (memberId == null) {
			return ResponseEntity.badRequest().build();
		}

		// 呼叫 Service 層獲取追蹤的案件
		List<Map<String, Object>> followedCases = followService.getFollowedCasesByMember(memberId);
		return ResponseEntity.ok(followedCases);
	}
}
