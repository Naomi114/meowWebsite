package tw.com.ispan.jwt;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String method = request.getMethod();
        if (!"OPTIONS".equals(method)) {
            // 是否有"已登入"的資訊
            String auth = request.getHeader("Authorization");
            JSONObject user = processAuthorizationHeader(auth);
            if (user == null || user.length() == 0) {
                // 沒有：是否要阻止使用者呼叫？
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "*");

                return false;
            }
        }
        return true;
    }

    private JSONObject processAuthorizationHeader(String auth) {
        if (auth != null && auth.length() != 0) {
            String token = auth.substring(7); // 移除前綴的'Bearer '
            String data = jsonWebTokenUtility.validateToken(token);
            if (data != null && data.length() != 0) {
                return new JSONObject(data);
            }
        }
        return null;
    }
}
