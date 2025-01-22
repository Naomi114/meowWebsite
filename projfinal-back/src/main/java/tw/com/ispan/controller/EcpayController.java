package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import tw.com.ispan.service.shop.OrderService;
import tw.com.ispan.util.EcpayFunctions;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.PaymentRequest;

import java.util.Map;

@RestController
@RequestMapping("/pages/ecpay") // 定义 ECPay 路径
@CrossOrigin // 支持跨域访问
public class EcpayController {

    @Autowired
    private EcpayFunctions ecpayFunctions;

    @Autowired
    private OrderService orderService; // 用于更新订单和购物车

    // 接收 ECPay 的回传数据
    @PostMapping("/return")
    public String ecpayReturn(@RequestBody String body) {
        System.out.println("ECPay return received at " + System.currentTimeMillis());
        System.out.println("Body: " + body);

        try {
            // 解析回传的 JSON 数据
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(body, Map.class);

            // 从回传数据中获取订单ID和支付状态
            String orderId = (String) responseMap.get("OrderId");
            String paymentStatus = (String) responseMap.get("PaymentStatus");

            // 根据支付状态处理订单
            if ("Success".equals(paymentStatus)) {
                // 更新订单状态为已支付
                orderService.updateOrderStatus(orderId, "PAID");

                // 清空购物车中的已购买商品
                orderService.clearCartForOrder(orderId);

                // 返回成功消息
                return "Payment processed successfully";
            } else {
                // 如果支付失败，返回失败消息
                return "Payment failed";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing payment return"; // 出现异常时返回错误信息
        }
    }

    // 提交支付请求，传递商品资料
    @PostMapping("/send")
    public String send(@RequestBody Map<String, Object> body) {
        try {
            // 将请求体中的商品数据转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String bodyJson = objectMapper.writeValueAsString(body);

            // 调用 EcpayFunctions 生成支付表单
            String form = ecpayFunctions.buildEcpayForm(bodyJson);

            // 返回生成的表单
            return form;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to convert data to JSON"; // 转换数据时发生错误
        }
    }

    // 确认订单支付
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Orders> processPayment(@PathVariable int orderId,
            @RequestBody PaymentRequest paymentRequest) {
        try {
            // 使用 orderService 来处理支付逻辑
            Orders updatedOrder = orderService.processPayment(orderId, paymentRequest);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 处理支付过程中出错
        }
    }
}
