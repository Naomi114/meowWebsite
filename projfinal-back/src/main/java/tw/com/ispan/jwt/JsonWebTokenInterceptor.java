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
		// 驗證成功後將 Token 中的資訊（如 custid）存入 HttpServletRequest 的屬性中，供後續 Controller 使用
		String method = request.getMethod();

		// 判斷是否是重定向
		if (response.getStatus() == HttpServletResponse.SC_MOVED_TEMPORARILY) {
			return true; // 放行重定向請求
		}

		if (!"OPTIONS".equals(method)) { // OPTIONS 請求通常是瀏覽器的預檢請求，不需要進行驗證
			// 是否有"已登入"的資訊
			String auth = request.getHeader("Authorization");
			System.out.println("攔截器2222");
			// 如果Heade沒有jwt相關資訊就請求失敗
			if (auth == null || !auth.startsWith("Bearer ")) {
				System.out.println("授權被攔截!!!!");
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
				// 此處拿到使用者資訊後，應先判斷使用者對於準備進入的controller是否有。payload:{"email":"alice@lab.com","memberId":3}

				// 將payload字串(為json格式的字串)轉換為 JSON
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(payload);

				// 從 payload 中提取 memberId (會轉換成int)
				// 管理員登入時token不會有memberId，因此要先檢查get("memberId")是否為null
				JsonNode memberIdNode = jsonNode.get("memberId");
				int memberId = (memberIdNode != null) ? memberIdNode.asInt(0) : 0;

				// 將 memberId 添加到請求屬性
				request.setAttribute("memberId", memberId);
				System.out.println("準備進到CONTROLLER中");
				return true;
			} else {
				// payload == null
				System.out.println(33333);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}
		return true;
	}
}
