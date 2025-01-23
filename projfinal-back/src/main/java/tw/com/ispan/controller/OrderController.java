package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.PaymentRequest;
import tw.com.ispan.repository.shop.OrderRequest;
import tw.com.ispan.service.shop.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 創建訂單
    @PostMapping("/create/{cartId}")
    public ResponseEntity<Orders> createOrder(@PathVariable int cartId, @RequestBody OrderRequest orderRequest) {
        orderRequest.setCartId(cartId); // 將購物車ID設置到訂單請求中
        Orders createdOrder = orderService.createOrder(orderRequest); // 根據購物車ID創建訂單
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder); // 返回創建的訂單
    }

    // 確認訂單支付
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Orders> processPayment(@PathVariable int orderId,
            @RequestBody PaymentRequest paymentRequest) {
        Orders updatedOrder = orderService.processPayment(orderId, paymentRequest); // 處理支付並更新訂單狀態
        return ResponseEntity.ok(updatedOrder); // 返回支付後的訂單
    }

    // 獲取訂單詳情
    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderDetails(@PathVariable int orderId) {
        Orders order = orderService.getOrderById(orderId); // 獲取訂單詳情
        return ResponseEntity.ok(order); // 返回訂單詳情
    }
}
