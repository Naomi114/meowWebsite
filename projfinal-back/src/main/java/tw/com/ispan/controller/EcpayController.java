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
@RequestMapping("/pages/ecpay") // Define ECPay path
public class EcpayController {

    @Autowired
    private EcpayFunctions ecpayFunctions;

    @Autowired
    private OrderService orderService; // Service to update orders and cart

    /**
     * Receive payment result from ECPay
     */
    @PostMapping("/return")
    public ResponseEntity<String> ecpayReturn(@RequestParam Map<String, String> params) {
        System.out.println("ECPay return received at " + System.currentTimeMillis());
        // Print all received parameters for debugging
        params.forEach((key, value) -> System.out.println(key + ": " + value));

        try {
            // Get MerchantTradeNo as ECPay transaction ID
            String ecpayTradeNo = params.get("MerchantTradeNo");
            if (ecpayTradeNo == null || ecpayTradeNo.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Missing MerchantTradeNo");
            }

            // Get Order ID (Your system's Order ID)
            String orderIdString = params.get("OrderId");
            if (orderIdString == null || orderIdString.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Missing OrderId");
            }

            Integer orderId = Integer.parseInt(orderIdString);
            String paymentStatus = params.get("RtnCode");

            // RtnCode '1' means payment successful
            if ("1".equals(paymentStatus)) {
                // Update order status
                orderService.updateOrderStatus(orderId, "已付款");

                // Save ECPay transaction ID
                orderService.updateEcpayTransactionId(orderId, ecpayTradeNo);

                // Get the order items and mark them as checked out
                List<OrderItemDTO> orderItemsDTO = orderService.getOrderItemsByOrderId(orderId);
                for (OrderItemDTO itemDTO : orderItemsDTO) {
                    orderService.updateOrderItemStatus(orderId, itemDTO.getProductId(), "已结账");
                }

                // Clear the cart for this order
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
     * Send payment request and pass product information
     */
    @PostMapping("/send")
    public String send(@RequestParam Map<String, String> body) {
        try {
            // Ensure the body contains orderId
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

            // Convert the body map to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String bodyJson = objectMapper.writeValueAsString(body);

            // Now call the buildEcpayForm method with a JSON string and the orderId
            String ecpayForm = ecpayFunctions.buildEcpayForm(bodyJson, orderId.toString());
            return ecpayForm;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to process request";
        }
    }

    /**
     * Confirm order payment
     */
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Map<String, Object>> processPayment(@PathVariable String orderId,
                                                 @RequestBody PaymentRequest paymentRequest) {
        try {
            Integer orderIdInt = Integer.parseInt(orderId);
            Orders updatedOrder = orderService.processPayment(orderIdInt, paymentRequest);

            // Construct response with only the necessary fields
            Map<String, Object> response = Map.of(
                "orderId", updatedOrder.getOrderId(),
                "finalPrice", updatedOrder.getFinalPrice(),
                "orderStatus", updatedOrder.getOrderStatus()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Generate the ECPay payment form and redirect to ECPay for payment
     */
    @PostMapping("/{orderId}/ecpay")
    public String initiatePayment(@PathVariable Long orderId) {
        // 从数据库中获取订单信息
        Orders order = orderService.getOrderDTOById(orderId);

        // 生成ECPay需要的参数
        String merchantID = "xxx"; // 替换为实际商户ID
        String orderID = order.getOrderId().toString();
        String amount = order.getFinalPrice().toString();
        String productName = order.getOrderItems().stream()
            .map(item -> item.getProductName())
            .collect(Collectors.joining(", "));

        // 生成ECPay表单HTML
        String formHtml = "<form action=\"https://payment.ecpay.com.tw/xxx\" method=\"POST\">" +
                          "<input type=\"hidden\" name=\"MerchantID\" value=\"" + merchantID + "\">" +
                          "<input type=\"hidden\" name=\"OrderID\" value=\"" + orderID + "\">" +
                          "<input type=\"hidden\" name=\"Amount\" value=\"" + amount + "\">" +
                          "<input type=\"hidden\" name=\"ProductName\" value=\"" + productName + "\">" +
                          // 添加其他必要字段
                          "<button type=\"submit\">Proceed to ECPay</button>" +
                          "</form>" +
                          "<script>document.forms[0].submit();</script>";

        // 返回HTML响应
        return formHtml;
    }
}
