package tw.com.ispan.service.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import java.io.IOException;

@Service
public class GoogleOAuthService {

    private static final String GOOGLE_PEOPLE_API_URL = "https://people.googleapis.com/v1/people/me";

    // 使用 access token 來取得使用者資料
    public String getUserProfile(String accessToken) throws IOException {
        // 使用 access token 來建立 GoogleCredentials
        AccessToken token = new AccessToken(accessToken, null); // null 表示不設置過期時間
        GoogleCredentials credentials = GoogleCredentials.create(token);

        // 創建 RestTemplate 用於發送 HTTP 請求
        RestTemplate restTemplate = new RestTemplate();

        // 設定 Authorization 標頭
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // 使用 access token 作為 Bearer Token

        // 建立 HTTP 請求的實體
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 發送 GET 請求，並獲取回應
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    GOOGLE_PEOPLE_API_URL,
                    HttpMethod.GET,
                    entity,
                    String.class);

            // 返回 JSON 字串
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IOException("Error while calling Google People API: " + e.getMessage(), e);
        }
    }

    public Payload verifyIdToken(String idToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyIdToken'");
    }
}