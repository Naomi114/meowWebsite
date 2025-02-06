package tw.com.ispan.service;

public class JwtService {
	
	
//	public Integer getMemberIdFromToken(String token) {
//	    
//		//header中Authorization: Bearer <JWT Token>
//		String jwt = token.replace("Bearer ", "");
//	    
//		
//		Claims claims = Jwts.parser()        ////創建一個 JWT 解析器
//	            .setSigningKey(secretKey)   // 設定JWT 密鑰(生成 JWT 時使用的密鑰，解析時需要同樣的密鑰來驗證 Token 的合法性。)
//	            .parseClaimsJws(jwt)        //解析 JWT Token，並驗證其簽名是否合法
//	            .getBody();                  //獲取 JWT 的內容（稱為 Claims），其中包含了自定義的 Payload 資訊（如 memberId）
//	    return claims.get("memberId").toString();   //從 JWT 的 Claims 中提取 memberId (生成 JWT 時，將 memberId 作為一部分的 Payload 存入)
//	}
//	
}
