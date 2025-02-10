package tw.com.ispan.service.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class GoogleOAuthService {

    private static final String CLIENT_ID = "your-google-client-id"; // 使用您的 Google Client ID

    // 驗證 ID Token 並返回 Payload
    public GoogleIdToken.Payload verifyIdToken(String idTokenString) throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        NetHttpTransport transport = new NetHttpTransport();

        // GoogleIdTokenVerifier 用來驗證 ID Token 是否有效
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID)) // 設定驗證的 Client ID
                .build();

        // 驗證 ID Token
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new IllegalArgumentException("Invalid ID token");
        }

        // 返回 ID Token 的 Payload
        return idToken.getPayload();
    }
}