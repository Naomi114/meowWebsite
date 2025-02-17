package tw.com.ispan.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import tw.com.ispan.dto.GoogleLoginRequestDTO;
import tw.com.ispan.dto.GoogleLoginResponseDTO;
import tw.com.ispan.service.google.GoogleOAuthService;

@RestController
@RequestMapping("/api/ajax/secure")
public class GoogleLoginController {

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @PostMapping("/google-login")
    public ResponseEntity<GoogleLoginResponseDTO> googleLogin(@RequestBody GoogleLoginRequestDTO request)
            throws IOException {

        // 輸出接收到的請求資訊
        System.out.println("GoogleLoginRequestDTO: " + request);

        // 檢查 ID Token 是否有效
        if (request.getIdtoken() == null || request.getIdtoken().isEmpty()) {
            System.err.println("Received empty or null ID Token.");
            return ResponseEntity.badRequest().body(createErrorResponse("ID Token is required"));
        }

        // 驗證 ID Token
        System.out.println("Verifying ID Token: " + request.getIdtoken());
        GoogleIdToken.Payload payload;
        try {

            // 使用 googleOAuthService 驗證 ID Token
            payload = googleOAuthService.verifyIdToken(request.getIdtoken());
            System.out.println("ID Token Verified, Payload: " + payload);
        } catch (Exception e) {
            System.err.println("ID Token verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid ID Token"));
        }

        // 從 Payload 中提取使用者資訊
        String email = payload.getEmail();
        String nickname = (String) payload.get("name");
        String userId = payload.getSubject(); // 取得 Google 的 userId
        String profilePicture = (String) payload.get("picture"); // 取得使用者的頭像

        // 返回成功的回應
        GoogleLoginResponseDTO response = createSuccessResponse(email, nickname, userId, profilePicture);
        return ResponseEntity.ok(response);
    }

    // Helper 方法：創建成功的回應
    private GoogleLoginResponseDTO createSuccessResponse(String email, String nickname, String userId,
            String profilePicture) {
        return new GoogleLoginResponseDTO(true, "登入成功", email, nickname, userId, profilePicture);
    }

    // Helper 方法：創建錯誤的回應
    private GoogleLoginResponseDTO createErrorResponse(String errorMessage) {
        return new GoogleLoginResponseDTO(false, errorMessage, null, null, null, null);
    }
}
