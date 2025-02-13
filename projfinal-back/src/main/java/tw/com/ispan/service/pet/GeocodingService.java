	package tw.com.ispan.service.pet;
	
	import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
	
	import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
	import org.springframework.stereotype.Service;
	import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.JsonMappingException;
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.ObjectMapper;
	
	import tw.com.ispan.util.LatLng;
	
	@Service
	public class GeocodingService {
	
		@Value("${google.api.key}")
		private String apiKey; // 從配置文件中讀取APIKey
	
		public LatLng getCoordinatesFromAddress(String address) throws JsonMappingException, JsonProcessingException, URISyntaxException, UnsupportedEncodingException {
	
			System.out.println("使用金鑰為" + apiKey);
			
			
			// RestTemplate 在處理 URL 時，會對已經編碼的地址部分再次進行URL編碼，導致%符號本身被編碼為 %25。這種情況被稱為「重複編碼（double encoding）
			// 為了避免RestTemplate重複編碼，應確保 URL 的編碼部分不會再次被處理
			// Spring提供了UriComponentsBuilder，它能自動處理URL的編碼並避免重複編碼問題，在轉為URL，避免丟進RestTemplate時重複編碼
			
			// 構建 URL
			String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
			        .queryParam("address", address)
			        .queryParam("key", "AIzaSyBYgeaSJSbLteuiSPtVAgdIV-yPLM4Av-c")
			        .queryParam("region", "tw")
			        .queryParam("language", "zh-TW")
			        .toUriString();
			
			System.out.println("請求網址為"+ url);
			
			// 將 URL 轉換為 URI，避免重複編碼
			URI uri = new URI(url);
			
			
			// RestTemplate是Spring提供的一個簡單的 HTTP 客戶端，用於與外部 API 進行交互作用。
			// getForEntity(String url, Class<T> responseType) 方法用於發送 HTTP GET
			// 請求，並將響應映射為指定的Java 類型
			// 返回一個 ResponseEntity<T> 對象，其中包含 HTTP 狀態碼、響應頭和響應主體。
			HttpHeaders headers = new HttpHeaders();
			headers.set("Referer", "http://localhost:8080");
			headers.add("Accept-Language", "zh-TW");
			HttpEntity<String> entity = new HttpEntity<>(headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(uri,
					HttpMethod.GET, entity, String.class);

			// response.getbody()可獲得返回的經緯度json物件
			// 使用 Jackson 的 ObjectMapper 將 JSON 字串轉換為 JsonNode，方便訪問經緯度數據 (json樹形結構看notion)
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response.getBody());
			
			//檢查返回狀態
			//geometry.bounds為該地址的邊界框;geometry.location為地址的精確中心點;geometry.viewport為建議的地圖視窗範圍，用於顯示該地址的最佳縮放比例
			String status = root.get("status").asText();
			if ("OK".equals(status)) {
				JsonNode location = root.get("results").get(0).get("geometry").get("location");
				double lat = location.get("lat").asDouble();
				double lng = location.get("lng").asDouble();
				LatLng latLng = new LatLng(lat, lng);
				System.out.println("獲取地址"+ address +"座標為" + latLng.toString());
				return latLng;

			} else {
				System.out.println("API 返回狀態: " + status);
				return null;
			}
		}
	
	}