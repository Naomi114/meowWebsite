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

	// 此方法為HandlerInterceptor介面底下抽象方法，當每個HTTP請求進入Controller前，Spring會調用這個方法來進行邏輯處理
	// 主要作用：
	// 1. 驗證請求中是否具有合法的 Token
	// 2. 如果驗證成功，將從 Token 提取的資訊存入 HttpServletRequest 中，以便後續的 Controller 使用
	// 3. 可在HttpServletRequest request注入數據進到controller中
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
				System.out.println(88888);
				// 將payload字串(為json格式的字串)轉換為 JSON
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(payload);

				// 從 payload 中提取 memberId (會轉換成int)
				Integer memberId = jsonNode.get("memberId").asInt();

				// 將 memberId 添加到請求屬性
				request.setAttribute("memberId", memberId);
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

//            //會把memberid提取出來
//            JSONObject tokenData = processAuthorizationHeader(auth);
//            if (tokenData == null || tokenData.length() == 0) {
//               // 沒有：是否要阻止使用者呼叫？
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);    //如果解析失敗或返回空資料（無效的 Token 或未提供 Token），回應 HTTP 403 禁止存取
//                response.setHeader("Access-Control-Allow-Credentials", "true");
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Access-Control-Allow-Headers", "*");
//
//                return false;
//            }

//        request.setAttribute("memberId", custid); // 設置 custid 屬性
//        return true;    //若驗證成功，返回 true，請求將繼續進入 Controller
//    }

//    //用於解析和驗證 Token，並從中提取用戶資訊
//    private JSONObject processAuthorizationHeader(String auth) throws JSONException {
//       
//    	if (auth != null && auth.length() != 0) {
//            String token = auth.substring(7);  // 移除前綴的'Bearer '，只取 <Token> 部分
//            String data = jsonWebTokenUtility.validateToken(token); //解析token有效性
//            if (data != null && data.length() != 0) {
//            	// 解碼 Token 並提取 custid 
//            	Integer memberId = jsonWebTokenUtility.extractCustIdFromPayload(data);
//            	
//            	JSONObject tokenData = new JSONObject();
//            	
//            	
//               	return tokenData;
//            }
//        }
//        return null;
//    }
}
