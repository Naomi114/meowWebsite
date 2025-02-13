package tw.com.ispan.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//攔截特定的API請求並驗證 Token
@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {
	@Autowired
	private JsonWebTokenUtility jsonWebTokenUtility;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("攔截器111111");
		
		String method = request.getMethod();
		
		 // 判斷是否是重定向
	    if (response.getStatus() == HttpServletResponse.SC_MOVED_TEMPORARILY) {
	        return true; // 放行重定向請求
	    }
		
		
		if (!"OPTIONS".equals(method)) { 
			
			String auth = request.getHeader("Authorization");

			if (auth == null || !auth.startsWith("Bearer ")) {
				System.out.println("授權被攔截");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}

			// 提取 Token
			String token = auth.substring(7); // 移除 'Bearer '
			System.out.println("token:" + token);

			// 驗證並解析出用戶資訊
			String payload = jsonWebTokenUtility.validateToken(token);
			System.out.println("payload:" + payload);
			if (payload != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(payload);

				// 從 payload 中提取 memberId (會轉換成int)
				//管理員登入時token不會有memberId，因此要先檢查get("memberId")是否為null
				JsonNode memberIdNode = jsonNode.get("memberId");
				int memberId = (memberIdNode != null) ? memberIdNode.asInt(0) : 0;

				request.setAttribute("memberId", memberId);
				return true;
			} else {
				// payload == null
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}
		return true;
	}
}

