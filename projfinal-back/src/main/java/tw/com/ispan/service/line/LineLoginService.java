package tw.com.ispan.service.line;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.dto.pet.LineUserProfile;
import tw.com.ispan.repository.admin.MemberRepository;

@Service
@Transactional
public class LineLoginService {

	private static final String VERIFY_TOKEN_URL = "https://api.line.me/oauth2/v2.1/verify"; // 拿取access_token
	private static final String TOKEN_URL = "https://api.line.me/oauth2/v2.1/token"; // 拿取tokenid

	@Value("${line.login.channel-id}")
	private String channelId;

	@Value("${line.login.channel-secret}")
	private String channelSecret;

	@Value("${line.login.redirect-uri}") // line callback
	private String redirectUri;

	@Autowired
	private MemberRepository memberRepository;

	// 產生授權url中的state(用於驗證)
	public String generateState() {
		// 生成唯一的 state
		String state = UUID.randomUUID().toString();

		return state;
	}

	// 產生授權url，返回給前端
	public String generateAuthorizeUrl(String state) {
		return "https://access.line.me/oauth2/v2.1/authorize" + "?response_type=code" + "&client_id=" + channelId
				+ "&redirect_uri=" + redirectUri + "&scope=profile%20openid%20email" + "&state=" + state;

		// 如果希望包含email資訊(為敏感資訊)，需要在超連結中加上&scope=profile openid email，再利用返回的acesstoken換取
		// ID Token並解析其中的email
		// %20 是 URL 編碼的 空格
	}

	// 拿line callback返回的code(短暫效期)，取得 access_token 和
	// id_token(用於表示有權利獲取line中用戶訊息)---------------------------------------------
	public Map<String, Object> exchangeCodeForTokens(String code) {

		RestTemplate restTemplate = new RestTemplate();

		// 構造request body
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("grant_type", "authorization_code");
		requestBody.add("code", code);
		requestBody.add("redirect_uri", redirectUri);
		requestBody.add("client_id", channelId);
		requestBody.add("client_secret", channelSecret);

		// 發送請求
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, new HttpHeaders());
		ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);
		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		}

		return null;
	}

	// 解析 id_token(內含userid, picture等等)---------------------------------------------
	public Map<String, Object> decodeIdToken(String idToken) {
		try {
			String[] parts = idToken.split("\\.");
			String payloadJson = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(payloadJson, Map.class);
		} catch (Exception e) {
			System.err.println("解析 id_token 失敗：" + e.getMessage());
			return null;
		}
	}

	// 登入後產生jwttoken儲存memberid作為會員身分驗證(直接使用jsonwebtokenutility中那個)---------------------------------------------

	// 調用 LINE 的 GET https://api.line.me/v2/profile獲取用戶訊息(如userId, displayName,
	// pictureUrl)
	// AccessToken要放在request header中 Authorization: Bearer <access_token>
	// LineUserProfile是用來解析返回的用戶信息的DTO------------------------------------------------------------------------------------------
	public LineUserProfile getUserProfile(String accessToken) {
		String url = "https://api.line.me/v2/profile";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		ResponseEntity<LineUserProfile> response = restTemplate.exchange(url, HttpMethod.GET,
				new org.springframework.http.HttpEntity<>(headers), LineUserProfile.class);

		return response.getBody();
	}

	// 使用 accessToken 換取 ID
	// Token---------------------------------------------------------------------
	public String getIdTokenFromAccessToken(String accessToken) {
		String url = VERIFY_TOKEN_URL + "?access_token=" + accessToken;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return (String) response.getBody().get("id_token");
		}
		return null;
	}

	// 透過lineid找到對應的memberid
	public Integer findMemberIdByLineUserId(String lineId) {
		Member member = memberRepository.findByLineId(lineId);
		return member != null ? member.getMemberId() : null;
	}

	// 檢查 LINE
	// 用戶是否已存在---------------------------------------------------------------------
	public boolean isLineUserExists(String lineId) {
		return memberRepository.existsByLineId(lineId);
	}

	// line
	// login時進行lineid和memberid的綁定---------------------------------------------------------------------
	// 已註冊會員：綁定 LINE 信息
	public void bindLineInfoToMember(Integer memberId, LineUserProfile profile) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("會員 ID 不存在：" + memberId));

		member.setLineId(profile.getUserId());
		member.setLineName(profile.getDisplayName());
		member.setLinePicture(profile.getPictureUrl());
		memberRepository.save(member);
	}

	// 非會員：新增 LINE
	// 用戶-----------------------------------------------------------------------------------------
	public void createLineOnlyUser(LineUserProfile profile) {
		Member member = new Member();
		member.setUserType(false); // false等於標記為LINE用戶
		member.setLineId(profile.getUserId());
		member.setLineName(profile.getDisplayName());
		member.setLinePicture(profile.getPictureUrl());

		// 自動填充默認值以滿足非空約束
		member.setNickName(profile.getDisplayName()); // 取line中暱稱
		member.setPassword("@Fakepswrd"); // 默認密碼
		member.setName(profile.getDisplayName());
		member.setEmail("unknown@line.com"); // 假設值
		member.setPhone("0000000000"); // 假設值
		member.setAddress("無"); // 假設值
		member.setBirthday(LocalDate.now()); // 默認填充當前日期
		member.setCreateDate(LocalDateTime.now());
		member.setUpdateDate(LocalDateTime.now());
		member.setUserType(true); // 類型為line用戶

		memberRepository.save(member);
	}
}
