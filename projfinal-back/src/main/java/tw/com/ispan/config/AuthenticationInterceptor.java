package tw.com.ispan.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    // 攔截每個請求，進行身份驗證檢查

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 取得請求中的身份驗證資訊，例如從 HTTP header 中提取 JWT Token
        String token = request.getHeader("Authorization");

        // 檢查是否存在有效的身份驗證資訊
        if (token == null || !isValidToken(token)) {
            // 如果沒有有效的 Token，回應未授權
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access. Please log in.");
            return false; // 阻止後續的處理
        }

        // 如果 Token 驗證通過，繼續處理請求
        return true;
    }

    // 模擬檢查 Token 的有效性
    private boolean isValidToken(String token) {
        // 在這裡你可以加入自己的 Token 驗證邏輯，或者調用某些服務來檢查 token 是否有效
        // 例如，檢查 token 是否符合某種模式或調用服務端來確認 Token 是否有效
        return "valid-token".equals(token); // 這是一個簡單的示例，實際應該與真正的驗證系統配合
    }

}