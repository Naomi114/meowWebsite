package tw.com.ispan.controller;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.PaymentRequest;
import tw.com.ispan.dto.OrderDTO;
import tw.com.ispan.repository.shop.OrderRequest;
import tw.com.ispan.service.shop.OrderService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 創建訂單
     */
    @PostMapping("/create/{cartId}")
    public ResponseEntity<?> createOrder(@PathVariable int cartId, @RequestBody OrderRequest orderRequest) {
        try {
            orderRequest.setCartId(cartId);
            Orders createdOrder = orderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("訂單創建失敗: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("訂單創建時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 處理訂單付款
     */
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<?> processPayment(@PathVariable int orderId, @RequestBody PaymentRequest paymentRequest) {
        try {
            Orders updatedOrder = orderService.processPayment(orderId, paymentRequest);
            return ResponseEntity.ok(updatedOrder);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("付款失敗: 訂單 " + orderId + " 不存在");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("付款失敗: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("處理付款時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 取得訂單詳情
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrderDTOById(orderId);
            return ResponseEntity.ok(orderDTO);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查詢失敗: 訂單 " + orderId + " 不存在");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("獲取訂單詳情時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 取得所有訂單
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Orders> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("獲取訂單列表時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 取消訂單
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam int orderId) {
        try {
            boolean isCancelled = orderService.cancelOrder(orderId);
            if (isCancelled) {
                return ResponseEntity.ok("訂單已成功取消");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法取消訂單: 訂單狀態不允許取消");
            }
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("取消失敗: 訂單 " + orderId + " 不存在");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("取消訂單時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 提交訂單
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitOrder(@RequestBody Map<String, Object> requestBody) {
        try {
            if (!requestBody.containsKey("cartId") || !requestBody.containsKey("member")
                    || !requestBody.containsKey("creditCard") || !requestBody.containsKey("shippingAddress")
                    || !requestBody.containsKey("selectedItems")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("提交失敗: 缺少必要參數 (cartId, member, creditCard, shippingAddress, selectedItems)");
            }

            try {
                int cartId = Integer.parseInt(requestBody.get("cartId").toString());
                int memberId = Integer.parseInt(requestBody.get("member").toString());
                String creditCard = requestBody.get("creditCard").toString();
                String shippingAddress = requestBody.get("shippingAddress").toString();

                // 解析 selectedItems
                List<Map<String, Object>> selectedItemsRaw = (List<Map<String, Object>>) requestBody
                        .get("selectedItems");
                List<Integer> selectedItems = selectedItemsRaw.stream()
                        .map(item -> Integer.parseInt(item.get("productId").toString()))
                        .collect(Collectors.toList());

                if (selectedItems.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("提交失敗: 必須選擇至少一個商品");
                }
                if (creditCard.trim().isEmpty() || shippingAddress.trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("提交失敗: 信用卡資訊或送貨地址不能為空");
                }

                boolean isSubmitted = orderService.submitOrder(cartId, memberId, creditCard, shippingAddress,
                        selectedItems);

                if (isSubmitted) {
                    return ResponseEntity.ok("訂單已成功提交");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法提交訂單: 訂單處理失敗");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("提交失敗: cartId 或 memberId 必須為數字");
            } catch (ClassCastException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("提交失敗: selectedItems 格式錯誤，請提供正確的 JSON 格式");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("提交訂單時發生錯誤: " + e.getMessage());
        }
    }
}