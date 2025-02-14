package tw.com.ispan.controller.line;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tw.com.ispan.dto.pet.LineUserProfile;
import tw.com.ispan.service.line.LineLoginService;
import tw.com.ispan.service.line.StateRedisService;

@CrossOrigin // 允許前端不同的主機或埠運行下可訪問這個contorller
@RestController
@RequestMapping("/line")
public class LineLoginController {

	@Value("${line.login.channel-id}")
	private String clientId;

	@Value("${line.login.channel-secret}")
	private String clientSecret;

	@Value("${line.login.redirect-uri}")
	private String redirectUri;

	@Autowired
	private StateRedisService stateRedisService;
	@Autowired
	private LineLoginService loginService;

	// 用於頁面載時產生用戶授權給line連結，回傳給前端
	// 在此分為兩種情境，如果用戶已註冊過，就連帶把memberId傳進來去和state綁再redis中，如果沒註冊過，就不傳進來
	@GetMapping("/authorize")
	public ResponseEntity<String> authorizeUser(@RequestParam(required = false) Integer memberId) {

		// 準備透過line登入不用驗證是會員!!!記得要排除於驗證jwt路徑中

		String state = loginService.generateState(); // 生成state

		// 如果有 memberId，則與 state 一起保存
		if (memberId != null) {
			stateRedisService.saveStateWithMemberId(state, memberId);
		} else {
			stateRedisService.saveState(state); // 僅保存 state
		}
		System.out.println("state=" + state);
		String authorizeUrl = loginService.generateAuthorizeUrl(state); // 構造授權 URL
		return ResponseEntity.ok(authorizeUrl); // 超連結返回給前端
	}

	// 此為用戶點擊授權連結後，line會發出一個callback
	// request(自定義於平台中)，url中夾帶code(line生成的)和state(我生成的)
	// https://yourdomain.com/callback?code=abcd1234&state=xyz789
	// state是OAuth 2.0和OpenID Connect授權流程中的一個安全機制，旨在防止跨站請求偽造（CSRF）攻擊，並確保授權請求的完整性
	@GetMapping("/callback")
	public ResponseEntity<String> handleCallback(@RequestParam String code, @RequestParam String state,
			HttpServletResponse response) throws IOException {

		System.out.println("用戶跳轉後line觸發callback!!");

		// 驗證 state(創造授權連結時主動加進去的亂數，驗證相同確保授權請求從用戶的瀏覽器發送過成未被篡改，也允許伺服器在回調中識別是哪個請求發起的授權流程)
		if (!stateRedisService.validateState(state)) {
			return ResponseEntity.badRequest().body("Invalid or expired state");
		}
		// 獲取memberId，如果為null則是非會員情境
		Integer memberId = stateRedisService.getMemberIdByState(state);
		System.out.println("找到會員id為" + memberId + "，即將綁定lineid");
		// 驗證成功且獲取memberId使用完後，刪除 state
		stateRedisService.deleteState(state);

		// 處理授權碼邏輯，換取accessToken
		String accessToken = loginService.exchangeCodeForAccessToken(code);
		if (accessToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to obtain access token");
		}
		// 使用 Access Token 獲取用戶信息(lineid, displayname, picture)
		LineUserProfile profile = loginService.getUserProfile(accessToken);
		if (profile == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to fetch user profile");
		}

		System.out.println("拿到Line用戶資訊" + profile.toString());

		// 先排除此用戶已經用Line登入過了，所以lineid已存在於member表中，此時就直接跳回原頁面
		if (loginService.isLineUserExists(profile.getUserId())) {
			System.out.println("LINE 用戶已存在，跳過新增操作");
		} else {
			// 第一次line登入~
			if (memberId != null) {
				// 已註冊會員情境：綁定 LINE ID 到會員
				loginService.bindLineInfoToMember(memberId, profile);
			} else {
				// 非會員情境：新增用戶記錄
				loginService.createLineOnlyUser(profile);
			}
		}

		// 需要把獲取到的line accessToken拿去前端儲存?(可以此從Line拿用戶資訊))，並讓用戶跳轉回原登入頁面
		String indexUri = "http://localhost:5173?token=" + accessToken;
		response.sendRedirect(indexUri);
		return ResponseEntity.ok("登入成功，歡迎 " + profile.getDisplayName());
	}
}
