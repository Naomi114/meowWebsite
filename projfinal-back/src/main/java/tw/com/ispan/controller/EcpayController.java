package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tw.com.ispan.util.EcpayFunctions;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController  // 使用 @RestController 來回應 JSON 資料
@RequestMapping("/pages/ecpay")  // 避免路徑衝突，兩者已統一路徑
@CrossOrigin  // 可以放在類別上，這樣所有方法都支持跨域
public class EcpayController {

    @Autowired
    private EcpayFunctions ecpayFunctions;

    // 接收 ECPay 的回傳資料
    @PostMapping("/return")
    public String ecpayReturn(@RequestBody String body) {
        System.out.println("ecpay return " + System.currentTimeMillis());
        System.out.println("body=" + body);

        // 根據 ECPay 的業務需求處理回傳內容，返回相應的頁面
        // 您可能需要進一步處理和驗證這些回傳數據
        return "Return Processed";  // 返回處理結果
    }

    // 發送資料給 ECPay
    @PostMapping("/send")
    public String send(@RequestBody Map<String, Object> body) {
        // 將 Map<String, Object> 轉換為 JSON 字串
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyJson = null;
        try {
            bodyJson = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: 無法轉換為 JSON";  // 返回錯誤訊息
        }

        // 呼叫 buildEcpayForm 並傳遞 JSON 字串
        try {
            String form = ecpayFunctions.buildEcpayForm(bodyJson);
            return form;  // 返回表單
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: 呼叫 ECPay 服務失敗";  // 服務呼叫失敗的錯誤訊息
        }
    }
}
