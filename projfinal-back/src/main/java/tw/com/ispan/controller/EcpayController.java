package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import tw.com.ispan.service.shop.OrderService;
import tw.com.ispan.util.EcpayFunctions;
import tw.com.ispan.dto.PaymentRequest;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.OrderItemDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pages/ecpay") // ECPay 路徑
public class EcpayController {

    @Autowired
    private EcpayFunctions ecpayFunctions;

    @Autowired
    private OrderService orderService; // 用於訂單和購物車處理

    /**
     * 處理 ECPay 付款結果
     */
    @PostMapping("/return")
    public ResponseEntity<String> ecpayReturn(@RequestParam Map<String, String> params) {
        System.out.println("ECPay return received at " + System.currentTimeMillis());
        // 打印所有接收到的參數，用於調試
        params.forEach((key, value) -> System.out.println(key + ": " + value));

        try {
            // 取得 MerchantTradeNo 當作 ECPay 交易編號
            String ecpayTradeNo = params.get("MerchantTradeNo");
            if (ecpayTradeNo == null || ecpayTradeNo.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Missing MerchantTradeNo");
            }

            // 取得訂單 ID（系統中的訂單 ID）
            String orderIdString = params.get("OrderId");
            if (orderIdString == null || orderIdString.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Missing OrderId");
            }

            Integer orderId = Integer.parseInt(orderIdString);
            String paymentStatus = params.get("RtnCode");

            // RtnCode '1' 代表支付成功
            if ("1".equals(paymentStatus)) {
                // 更新訂單狀態
                orderService.updateOrderStatus(orderId, "已付款");

                // 儲存 ECPay 交易編號
                orderService.updateEcpayTransactionId(orderId, ecpayTradeNo);

                // 取得訂單商品並標記為已結帳
                List<OrderItemDTO> orderItemsDTO = orderService.getOrderItemsByOrderId(orderId);
                for (OrderItemDTO itemDTO : orderItemsDTO) {
                    orderService.updateOrderItemStatus(orderId, itemDTO.getProductId(), "已结账");
                }

                // 清除該訂單的購物車
                orderService.clearCartForOrder(orderId);

                return ResponseEntity.ok("Payment processed successfully");
            } else {
                return ResponseEntity.status(400).body("Payment failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing payment return");
        }
    }

    /**
     * 發送支付請求並傳送商品資訊
     */
    @PostMapping("/send")
    public String send(@RequestParam Map<String, String> body) { // 使用 @RequestParam 處理 x-www-form-urlencoded
        try {
            // 確保 body 包含 orderId
            String orderIdString = body.get("orderId");
            if (orderIdString == null || orderIdString.isEmpty()) {
                return "Error: Missing orderId";
            }

            Integer orderId;
            try {
                orderId = Integer.parseInt(orderIdString);
            } catch (NumberFormatException e) {
                return "Error: Invalid orderId format";
            }

            // 將 body map 轉換為 JSON 字串
            ObjectMapper objectMapper = new ObjectMapper();
            String bodyJson = objectMapper.writeValueAsString(body);

            // 呼叫 buildEcpayForm 方法生成 ECPay 表單
            String ecpayForm = ecpayFunctions.buildEcpayForm(bodyJson, orderId.toString());
            return ecpayForm;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to process request";
        }
    }

    /**
     * 確認訂單支付
     */
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Map<String, Object>> processPayment(@PathVariable String orderId,
            @RequestBody PaymentRequest paymentRequest) {
        try {
            Integer orderIdInt = Integer.parseInt(orderId);
            Orders updatedOrder = orderService.processPayment(orderIdInt, paymentRequest);

            // 建構回應資料，僅包含必要的欄位
            Map<String, Object> response = Map.of(
                    "orderId", updatedOrder.getOrderId(),
                    "finalPrice", updatedOrder.getFinalPrice(),
                    "orderStatus", updatedOrder.getOrderStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 生成 ECPay 付款表單並跳轉至 ECPay 進行付款
     */
    @PostMapping("/{orderId}/ecpay")
    public String initiatePayment(@PathVariable Long orderId) {
        // 從資料庫中獲取訂單資訊
        Orders order = orderService.getOrderDTOById(orderId);

        // 生成 ECPay 需要的參數
        String merchantID = "xxx"; // 替換為實際商戶ID
        String orderID = order.getOrderId().toString();
        String amount = order.getFinalPrice().toString();
        String productName = order.getOrderItems().stream()
                .map(item -> item.getProductName())
                .collect(Collectors.joining(", "));

        // 生成 ECPay 表單 HTML
        String formHtml = "<form action=\"https://payment.ecpay.com.tw/xxx\" method=\"POST\">" +
                "<input type=\"hidden\" name=\"MerchantID\" value=\"" + merchantID + "\">" +
                "<input type=\"hidden\" name=\"OrderID\" value=\"" + orderID + "\">" +
                "<input type=\"hidden\" name=\"Amount\" value=\"" + amount + "\">" +
                "<input type=\"hidden\" name=\"ProductName\" value=\"" + productName + "\">" +
                // 添加其他必要字段
                "<button type=\"submit\">Proceed to ECPay</button>" +
                "</form>" +
                "<script>document.forms[0].submit();</script>";

        // 返回 HTML 回應
        return formHtml;
    }
}
