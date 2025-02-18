package tw.com.ispan.controller.line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.ispan.service.line.LineBindingService;
import tw.com.ispan.service.line.LineNotificationService;

//用來接收line平台中因會員和linebot互動產生的Webhook回調事件，LINE 平台會以 POST 的方式將事件推送到你的 Webhook URL (去line developer修改成正確開放網域)
//記得這個功能因為要用公開網域來讓Line連到我的後端，因此要先用ngrok開放這個專案運行的本地網域
//有時ngrok開不起來要先檢查是否有其他正在運行 再使用 ngrok http 8080 開放
@RestController
@RequestMapping("/webhook")
public class LineWebhookController {

    @Value("${front.domainName.url}")
    private String frontIndex;

    @Value("${ngrok.url}")
    private String ngrokUrl;

    @Autowired
    private LineNotificationService lineNotificationService;

    @Autowired
    private LineBindingService lineBindingService;

    // 接收並處理來自 LINE Messaging API Webhook 的回調事件
    // 步驟:用戶點選機器人按鈕後並自動追蹤機器人帳號後，和平台互動完，會傳回此request進來(初次追蹤就會發送一條type為follow的request進來)
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {

        System.out.println("hihihi");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);

            // 確保事件是 follow
            String eventType = jsonNode.at("/events/0/type").asText();
            if ("follow".equals(eventType)) {
                String lineId = jsonNode.at("/events/0/source/userId").asText();
                System.out.println("新追蹤者: " + lineId);

                // 將member表中followed改為true
                lineBindingService.updateFollowed(lineId);
               

                // line中發送跳轉連結給用戶 (網頁用戶則此追蹤line qrcode是新開一個分頁)
                String returnUrl = frontIndex; // 替換為元網頁的 URL
                String message = "感謝您追蹤！點擊以下連結返回網頁：" + returnUrl;
                lineNotificationService.sendNotification(lineId, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }
        return ResponseEntity.ok("Webhook processed");
    }
}


// 回傳HTTP response，LINE平台需要接收到HTTP
// 200回應已確定Webhook請求處理成功。如果LINE平台未收到成功回應，將視為事件處理失敗，並可能重試推送

//當用戶點選案件變更訊息的察看按鈕，會回傳此給後端
//{
//	  "events": [
//	    {
//	      "type": "postback",
//	      "replyToken": "xxxxxxxxxxxxxxxx",
//	      "source": {
//	        "userId": "Uxxxxxxxxxxxxxxxxxxxxx",
//	        "type": "user"
//	      },
//	      "postback": {
//	        "data": "action=view_case&caseId=123"
//	      }
//	    }
//	  ]
//	}