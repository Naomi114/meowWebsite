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
@CrossOrigin
@RestController
@RequestMapping("/api/line")
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
