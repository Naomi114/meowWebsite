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
@RequestMapping("/ajax/secure")
public class GoogleLoginController {

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @PostMapping("/google-login")
    public ResponseEntity<GoogleLoginResponseDTO> googleLogin(@RequestBody GoogleLoginRequestDTO request) {
        try {
            // 驗證 ID Token
            GoogleIdToken.Payload payload = googleOAuthService.verifyIdToken(request.getIdToken());

            // 從 Payload 中提取使用者資訊
            String email = payload.getEmail();
            String nickname = (String) payload.get("name");
            String userId = payload.getSubject(); // 取得 Google 的 userId
            String profilePicture = (String) payload.get("picture"); // 取得使用者的頭像

            // 這裡可以進一步處理，例如保存使用者資料到資料庫，生成 JWT token 等

            // 返回成功的回應
            GoogleLoginResponseDTO response = new GoogleLoginResponseDTO(
                    true,
                    "登入成功",
                    email,
                    nickname,
                    userId,
                    profilePicture);
            return ResponseEntity.ok(response);

        } catch (GeneralSecurityException | IOException e) {
            // 驗證失敗，返回錯誤的回應
            GoogleLoginResponseDTO response = new GoogleLoginResponseDTO(
                    false,
                    "登入失敗: " + e.getMessage(),
                    null,
                    null,
                    null,
                    null);
            return ResponseEntity.status(400).body(response);
        }
    }
}
