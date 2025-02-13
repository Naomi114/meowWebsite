package tw.com.ispan.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import tw.com.ispan.service.shop.EmailService;
import tw.com.ispan.service.shop.OrderService;
import tw.com.ispan.util.EcpayFunctions;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.shop.OrderItemDTO;
import tw.com.ispan.dto.shop.PaymentRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pages/ecpay") // ECPay 路徑
public class EcpayController {

    @Autowired
    private EcpayFunctions ecpayFunctions;

    @Autowired
    private OrderService orderService; // 用於訂單和購物車處理

    @Autowired
    private EmailService emailService;

    /**
     * 處理 ECPay 付款結果
     */
    @PostMapping("/return")
    public ResponseEntity<String> ecpayReturn(@RequestParam Map<String, String> params) {
        System.out.println("🔹 ECPay 回傳收到 at " + System.currentTimeMillis());
        params.forEach((key, value) -> System.out.println(key + ": " + value));

        try {
            String ecpayTransactionId = params.get("TradeNo");
            if (ecpayTransactionId == null || ecpayTransactionId.isEmpty()) {
                return ResponseEntity.status(400).body("❌ Error: Missing TradeNo");
            }

            String merchantTradeNo = params.get("MerchantTradeNo");
            if (merchantTradeNo == null || merchantTradeNo.isEmpty()) {
                return ResponseEntity.status(400).body("❌ Error: Missing MerchantTradeNo");
            }

            // 找到訂單
            Optional<Orders> orderOpt = orderService.getOrderByMerchantTradeNo(merchantTradeNo);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(400).body("❌ Error: Order not found");
            }
            Orders order = orderOpt.get();

            String paymentStatus = params.get("RtnCode");
            System.out.println("🔹 ECPay 付款狀態: " + paymentStatus);

            if ("1".equals(paymentStatus)) {
                // 更新訂單狀態
                orderService.updateOrderStatus(order.getOrderId(), "已付款");
                orderService.updateEcpayTransactionId(order.getOrderId(), ecpayTransactionId);
                System.out.println("✅ 訂單已更新為已付款");

                // 使用固定的 Email 進行測試
                String testEmail = "abc61130208@yahoo.com.tw"; // 預設測試信箱

                System.out.println("📩 發送確認信到: " + testEmail);
                try {
                    // 生成郵件內容
                    StringBuilder emailContent = new StringBuilder();
                    emailContent.append("<h1>訂單確認</h1>")
                            .append("<p>您的訂單 <strong>").append(merchantTradeNo).append("</strong> 已成功付款！</p>")
                            .append("<p>交易編號：" + ecpayTransactionId + "</p>")
                            .append("<p>訂單金額：NT$ " + order.getFinalPrice() + "</p>")
                            .append("<p>會員ID：" + order.getOrderId() + "</p>")
                            .append("<p>訂單狀態：" + order.getOrderStatus() + "</p>")
                            .append("<p>出貨狀態：").append(order.getOrderItems().stream()
                                    .map(item -> item.getStatus()) // 顯示商品的出貨狀態
                                    .collect(Collectors.joining(", ")))
                            .append("</p>")
                            .append("<h2>訂單項目</h2><ul>");

                    // 顯示訂單內的商品
                    for (OrderItem item : order.getOrderItems()) {
                        emailContent.append("<li>")
                                .append("商品名稱：").append(item.getProductName()).append("<br>")
                                .append("數量：").append(item.getOrderQuantity()).append("<br>")
                                .append("單價：NT$ ").append(item.getPurchasedPrice()).append("<br>")
                                .append("商品狀態：").append(item.getStatus()).append("<br>")
                                .append("</li>");
                    }

                    emailContent.append("</ul>")
                            .append("<p>感謝您的購買！</p>");

                    emailService.sendEmail(testEmail, "訂單確認通知", emailContent.toString());
                    System.out.println("✅ 信件已發送");
                } catch (Exception e) {
                    System.out.println("❌ 發送信件失敗: " + e.getMessage());
                    e.printStackTrace();
                }

                return ResponseEntity.ok("✅ Payment processed successfully");
            } else {
                return ResponseEntity.status(400).body("❌ Payment failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Error processing payment return");
        }
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendTestEmail() {
        emailService.sendEmail("abc61130208@yahoo.com.tw", "測試郵件", "<h1>這是一封測試郵件</h1>");
        return ResponseEntity.ok("Email sent successfully");
    }

    /**
     * 發送支付請求並傳送商品資訊
     */
    @PostMapping("/send")
    public String send(@RequestParam Map<String, String> body) {
        try {
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

            String merchantTradeNo = "" + System.currentTimeMillis(); // Generate MerchantTradeNo

            // Store MerchantTradeNo in the database
            orderService.updateMerchantTradeNo(orderId, merchantTradeNo);

            ObjectMapper objectMapper = new ObjectMapper();
            String bodyJson = objectMapper.writeValueAsString(body);

            String ecpayForm = ecpayFunctions.buildEcpayForm(bodyJson, merchantTradeNo);
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
        Orders order = orderService.getOrderDTOById(orderId);

        String merchantID = "xxx";
        String orderID = order.getOrderId().toString();
        String amount = order.getFinalPrice().toString();
        String productName = order.getOrderItems().stream()
                .map(item -> item.getProductName())
                .collect(Collectors.joining(", "));

        String formHtml = "<form action=\"https://payment.ecpay.com.tw/xxx\" method=\"POST\">" +
                "<input type=\"hidden\" name=\"MerchantID\" value=\"" + merchantID + "\">" +
                "<input type=\"hidden\" name=\"OrderID\" value=\"" + orderID + "\">" +
                "<input type=\"hidden\" name=\"Amount\" value=\"" + amount + "\">" +
                "<input type=\"hidden\" name=\"ProductName\" value=\"" + productName + "\">" +
                "<button type=\"submit\">Proceed to ECPay</button>" +
                "</form>" +
                "<script>document.forms[0].submit();</script>";

        return formHtml;
    }
}
