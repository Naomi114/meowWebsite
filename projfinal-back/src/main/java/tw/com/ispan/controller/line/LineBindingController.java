package tw.com.ispan.controller.line;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.dto.pet.RescueCaseResponse;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.LineTemporaryBindingRepository;
import tw.com.ispan.service.line.LineBindingService;
import tw.com.ispan.service.line.LineNotificationService;
import tw.com.ispan.service.line.RedisService;

//用來檢查用戶是否line登入、追蹤平台商家
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/line")
public class LineBindingController {

    @Value("${lineBot}")
    private String lineBotUrl;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LineBindingService lineBindingService;
    @Autowired
    private LineNotificationService lineNotificationService;
    @Autowired
    private LineTemporaryBindingRepository lineTemporaryBindingRepository;
    @Autowired
    private RedisService redisService;

    // 如果會員想執行line通知功能，點選Linebot按鈕前會先被判斷是否已使用Line登入過(表示memberid和lineid有綁定)，沒有則先提醒進行Line登入
    // 此步驟用來判斷memberId有無綁定lineId
    @GetMapping("/checkBinding")
    public RescueCaseResponse checkLineBinding(@RequestHeader("Authorization") String token,
            @RequestAttribute("memberId") Integer memberId) {

        RescueCaseResponse response = new RescueCaseResponse();

        if (memberId == null) {
            response.setSuccess(false);
            response.setMessage("請先登入");
            return response;
        }

        boolean isBound = lineBindingService.isLineBound(memberId);

        if (isBound) {
            response.setSuccess(true);
            response.setMessage("已綁定 LINE");
            return response;
        } else {
            response.setSuccess(false);
            response.setMessage("尚未綁定 LINE，請進行 LINE 登入");
            return response;
        }
    }

    // 用於檢查用戶是否有追蹤商家帳號
    @GetMapping("/checkFollow")
    public ResponseEntity<Map<String, Object>> checkFollow(@RequestParam("memberId") Integer memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 呼叫 Service 層檢查追蹤狀態
            boolean isFollowed = lineBindingService.checkFollow(memberId);
            System.out.println("isFollowed: " + isFollowed);

            if (isFollowed) {
                response.put("status", "success");
                response.put("followed", true);
                response.put("message", "連動成功");
            } else {
                response.put("status", "success");
                response.put("followed", false);
                response.put("message", "尚未連動成功");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "發生錯誤，無法確認追蹤狀態");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

// //step1 此為用戶點選跳轉超連結後的中間層，用於紀錄夾帶參數memberId到redis暫存資料庫，存完重定向到 LINE 的官方連結
// @GetMapping
// public void redirectToLine(@RequestHeader("Authorization") String token,
// @RequestParam("memberId") String memberId, HttpServletResponse response)
// throws IOException {

// //追蹤功能需要認定為會員，因此header需要帶有token

// // 提前保存 memberId，方式採用暫時存儲在Redis
// redisService.saveMemberId(memberId);

// // 重定向到 LINE 的 URL，仍然挾帶memberId(作用為何???)
// String lineRedirectUrl = lineBotUrl;
// response.sendRedirect(lineRedirectUrl);

// // 發送綁定確認消息給用戶(內含memberId)
// lineBindingService.sendBindingMessageToUser(memberId);

// }

// //第一步，會員點選綁定要求
// @GetMapping("/bindRequest")
// public ResponseEntity<String> bindRequest(@RequestHeader("Authorization")
// String token, @RequestAttribute("memberId") Integer memberId) {

// // 1.驗證token(能通過JsonWebTokenInterceptor攔截器即驗證)，表示為會員元，並從中提取memberId
// System.out.println("此為會員id" + memberId + "執行line綁定請求");

// // 2. 伺服器生成綁定 Token ，此時會員表中此會員ID資料會綁定上綁定 Token
// // String url = lineBindingService.generateBindingLink(memberId);
// //將http://localhost:8080/line/bind?token=<bindingToken>

// return ResponseEntity.ok("請稍後，完成綁定流程...");
// }

// //用戶點擊綁定鏈接後，系統需要處理該請求，完成 LINE ID 與會員的綁定
// @GetMapping("/bindComplete")
// public ResponseEntity<String> bindComplete(@RequestParam String bindingToken,
// @RequestParam String authToken) {

// // 從暫存表中查找 token
// Optional<LineTemporaryBinding> bindingOpt =
// lineTemporaryBindingRepository.findByBindingToken(bindingToken);
// if (bindingOpt.isEmpty()) {
// return ResponseEntity.badRequest().body("綁定token不存在於資料表中，請用戶重新加入好友");
// }

// LineTemporaryBinding binding = bindingOpt.get();

// // 驗證 token 是否過期
// if (binding.getExpiryTime().isBefore(LocalDateTime.now())) {
// return ResponseEntity.badRequest().body("綁定token已過期！");
// }

// // 綁定 LINE ID 和 memberId
// String lineId = binding.getLineId();
// //到底怎麼拿到memberid!!!!!
// // memberRepository.bindLineIdandMemberId(memberId, lineId);

// // 刪除暫存的綁定記錄
// lineTemporaryBindingRepository.delete(binding);

// return ResponseEntity.ok("綁定成功！");
// }

// //https://line.me/R/ti/p/@310pndih 為linebot跳轉超連結，跳轉後自動追蹤(qrcode放置於專案data資料夾中)
