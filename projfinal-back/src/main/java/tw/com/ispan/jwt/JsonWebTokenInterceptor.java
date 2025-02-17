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
		System.out.println("攔截器：進入 preHandle 方法");

		// 打印請求方法（GET、POST等）
		String method = request.getMethod();
		System.out.println("攔截器：請求方法 " + method);

		// 判斷是否是重定向 (這裡可能是處理CORS請求的部分)
		if (response.getStatus() == HttpServletResponse.SC_MOVED_TEMPORARILY) {
			System.out.println("攔截器：檢測到重定向，放行請求");
			return true;
		}

		// 當請求方法不是 "OPTIONS" 時，進行Token檢查
		if (!"OPTIONS".equals(method)) { // OPTIONS 請求通常是瀏覽器的預檢請求
			// 檢查Authorization header 是否存在
			String auth = request.getHeader("Authorization");
			if (auth == null || !auth.startsWith("Bearer ")) {
				System.out.println("攔截器：授權資訊缺失或格式錯誤");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}

			// 提取 token
			String token = auth.substring(7); // 去掉 "Bearer " 部分
			System.out.println("攔截器：提取到 token：" + token);

			// 驗證並解析 token，獲取 payload
			String payload = jsonWebTokenUtility.validateToken(token);
			System.out.println("攔截器：Request Content-Type: " + request.getContentType());
			System.out.println("攔截器：解析到的 payload: " + payload);

			if (payload != null) {
				// 如果 payload 有效，設置用戶資料並放行請求
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(payload);
				Integer memberId = jsonNode.get("memberId").asInt();
				request.setAttribute("memberId", memberId);

				// 放行請求，進入後端控制器
				System.out.println("攔截器：Token 驗證通過，放行請求至控制器，memberId：" + memberId);
				return true;
			} else {
				// 如果 token 驗證失敗，返回 403
				System.out.println("攔截器：Token 驗證失敗，返回 403");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}

		// 如果是 OPTIONS 請求，直接放行
		System.out.println("攔截器：OPTIONS 請求，放行請求");
		return true;
	}
}
