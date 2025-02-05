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
import java.util.Optional;
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
        params.forEach((key, value) -> System.out.println(key + ": " + value));

        try {
            String ecpayTransactionId = params.get("TradeNo"); // Get the transaction ID (TradeNo) from ECPay
            if (ecpayTransactionId == null || ecpayTransactionId.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Missing TradeNo");
            }

            String merchantTradeNo = params.get("MerchantTradeNo"); // Get the MerchantTradeNo (which you sent earlier)
            if (merchantTradeNo == null || merchantTradeNo.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Missing MerchantTradeNo");
            }

            // Find order by MerchantTradeNo
            Optional<Orders> orderOpt = orderService.getOrderByMerchantTradeNo(merchantTradeNo);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Order not found");
            }
            Orders order = orderOpt.get();

            String paymentStatus = params.get("RtnCode");
            if ("1".equals(paymentStatus)) {
                // Update the order status and store the transaction ID from ECPay
                orderService.updateOrderStatus(order.getOrderId(), "已付款"); // Update order status to '已付款'
                orderService.updateEcpayTransactionId(order.getOrderId(), ecpayTransactionId); // Store the transaction
                                                                                               // ID in the database
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
