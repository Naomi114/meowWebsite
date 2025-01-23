// package tw.com.ispan.controller.line;

// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import tw.com.ispan.repository.admin.MemberRepository;
// import tw.com.ispan.service.line.LineBindingService;
// import tw.com.ispan.service.line.LineNotificationService;
// import tw.com.ispan.service.line.RedisService;

// //用來接收line平台中因和linebot互動產生的Webhook回調事件，LINE 平台會以 POST 的方式將事件推送到你的 Webhook URL
// @RestController
// @RequestMapping("/webhook")
// public class LineWebhookController {
	
// 	@Value("${DomainName.url}")
// 	private String domainNameUrl;
	
// 	@Value("${ngrok.url}")
// 	private String ngrokUrl;
	
// 	@Autowired
// 	private MemberRepository memberRepository;
// 	@Autowired
// 	private LineBindingService lineBindingService;
// 	@Autowired
// 	LineNotificationService lineNotificationService;
// 	@Autowired
// 	private RedisService redisService;

// 	// 接收並處理來自 LINE Messaging API Webhook 的回調事件
// 	// 步驟:用戶點選機器人按鈕後並自動追蹤機器人帳號後，和平台互動完，會傳回此request進來(初次追蹤就會發送一條type為follow的進來)
// 	@PostMapping
// 	public ResponseEntity<String> handleWebhook(@RequestBody String payload) {

// 		System.out.println("hihihi");
// 		// 解析 Webhook 請求獲得lineId
// 		ObjectMapper objectMapper = new ObjectMapper();
// 		JsonNode jsonNode = objectMapper.readTree(payload);
// 		String lineId = jsonNode.at("/events/0/source/userId").asText();
		
// 		//發送綁定訊息回去給用戶，讓用戶點擊後完成綁定
// 		redisService.createBindingFlexMessage();
		
		
// 		// 從暫存中查詢對應的 memberId
// 		String memberId = redisService.getMemberId(lineId);

// 		// 從 Redis 查詢 memberId
// 		String memberId = redisTemplate.opsForValue().get("LINE_BIND_" + lineId);
		
// 		//!!!!!!改成不要在這裡就綁定Lineid，而是讓controller繼續redirect到前端後(要指向一個新頁面使得可以執行onMounted())，把lineid資訊包在裡面，並且在onMonuted裡面
// 		//將jwt中的memberId和lineid綁定!!!!

// 		for (Map<String, Object> event : events) {

// 			// 從事件的 Map 中提取 type 字段，這個字段表示事件的類型，其中事件follow：用戶加為好友或解除封鎖。
// 			// message：用戶發送消息。unfollow：用戶取消好友。join：Bot 被邀請加入群組或聊天室。
// 			String type = (String) event.get("type");

// 			if ("follow".equals(type)) {
// 				// 從事件中提取 source 字段，這是關於事件來源的數據，再提取用戶的 LINE ID
// 				Map<String, Object> source = (Map<String, Object>) event.get("source");
// 				String lineId = (String) source.get("userId");

// 				// 在此獲得lineId後，透過產生token插入臨時表中，綁定lineId和token。並產生要發送給用戶line訊息的綁定鏈結
// 				String bindingToken = lineBindingService.generateBindingLinkForLineId(lineId);
// 				String bindingLink = ngrokUrl+ "/line/bindComplete?token=" + bindingToken;

// 				// 發送綁定鏈接到用戶的 LINE
// 				lineNotificationService.sendBindingMessage(lineId, bindingLink);

// 				System.out.println("新用戶的 LINE ID: " + lineId + " 已收到綁定消息。");

// 			}
// 		}

// 		// 回傳HTTP response，LINE平台需要接收到HTTP
// 		// 200回應已確定Webhook請求處理成功。如果LINE平台未收到成功回應，將視為事件處理失敗，並可能重試推送
// 		return ResponseEntity.ok("Webhook received");
// 	}
// }


