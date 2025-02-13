package tw.com.ispan.jwt;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;

@Component
public class JsonWebTokenUtility {
	
	//設定jwt生成後，有效時間
	@Value("${jwt.token.expire}")
	private long expire;

	private String issuer = "ispan";
	private byte[] sharedKey; // 為用在簽章的金鑰

	
	@PostConstruct
	public void init() {
		sharedKey = new byte[64];

		// TODO：可以使用其他方式產生金鑰內容
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(sharedKey);
	}
	
	public String createToken(String data) {
		Instant now = Instant.now();
		Instant expire = now.plusSeconds(this.expire * 60);
		try {
			// 建立HMAC signer
			JWSSigner signer = new MACSigner(sharedKey);    

			// 準備JWT主體
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
					.issuer(issuer)                                 
					.issueTime(Date.from(now))                      
					.expirationTime(Date.from(expire))              
					.subject(data)                                  //夾帶資訊
					.build();

			// 建立HMAC保護的JWT，使用 JWSAlgorithm.HS512簽名算法，搭配金鑰
			SignedJWT signedJWT = new SignedJWT(
					new JWSHeader(JWSAlgorithm.HS512),
					claimsSet);
			signedJWT.sign(signer);

			// 產生Token
			return signedJWT.serialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//驗證 Token 的有效性，並從中提取資訊
	public String validateToken(String token) {
		try {
			// 建立HMAC verifier
			JWSVerifier verifier = new MACVerifier(sharedKey);

			// 解析JWS
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
			System.out.println(signedJWT.verify(verifier));         
	        // 驗證簽名通過以及時間尚未過期
			if (signedJWT.verify(verifier) && new Date().before(claimsSet.getExpirationTime())) {
				System.out.println("簽章和效期驗證通過"); 
				
				// 返回token內重要資訊（通常是用戶ID）
				String subject = claimsSet.getSubject();
				return subject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("token驗證失敗"); 
		return null;
	}
	
	
}
