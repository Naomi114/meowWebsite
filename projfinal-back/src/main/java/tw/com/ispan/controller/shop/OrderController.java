package tw.com.ispan.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.shop.OrderDTO;
import tw.com.ispan.dto.shop.PaymentRequest;
import tw.com.ispan.repository.shop.OrderRequest;
import tw.com.ispan.service.shop.EmailService;
import tw.com.ispan.service.shop.OrderService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private EmailService emailService;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("訂單創建時發生錯誤: " + e.getMessage());
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
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("付款失敗: 訂單 " + orderId + " 不存在");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("付款失敗: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("處理付款時發生錯誤: " + e.getMessage());
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
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查詢失敗: 訂單 " + orderId + " 不存在");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取訂單詳情時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 取得會員的所有訂單
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getOrdersByMember(@PathVariable int memberId) {
        try {
            List<Orders> orders = orderService.getOrdersByMemberId(memberId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取訂單時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 管理員取得所有訂單
     * 管理員可以查詢所有會員的訂單
     */
    @GetMapping("/admin")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Orders> allOrders = orderService.getAllOrders();
            return ResponseEntity.ok(allOrders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取所有訂單時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 更新訂單狀態
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable int orderId, @RequestBody Map<String, String> request) {
        try {
            String newStatus = request.get("orderStatus");
            Orders order = orderService.updateOrderStatus(orderId, newStatus);

            // 只在「備貨中」或「出貨中」時發送郵件通知會員
            boolean emailSent = false;
            if ("備貨中".equals(newStatus) || "出貨中".equals(newStatus)) {
                String emailContent = generateEmailContent(order, newStatus);
                try {
                    // 將發送郵件的過程打印出來以便於調試
                    String email = "abc61130208@yahoo.com.tw"; // Bind email address here
                    System.out.println("Sending email to: " + email);
                    emailService.sendEmail(email, "您的訂單狀態已更新", emailContent);
                    emailSent = true; // Mark email as sent
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("郵件發送失敗: " + e.getMessage());
                }
            }

            // Add a confirmation about email sending
            String responseMessage = "訂單狀態已更新";
            if (emailSent) {
                responseMessage += "，並且郵件已成功發送";
            } else {
                responseMessage += "，但未發送郵件（狀態未為「備貨中」或「出貨中」）";
            }

            return ResponseEntity.ok(responseMessage);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("更新失敗: 訂單 " + orderId + " 不存在");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新訂單狀態時發生錯誤: " + e.getMessage());
        }
    }

    // 簡化的郵件內容
    private String generateEmailContent(Orders order, String status) {
        return "<h3>您的訂單狀態已更新</h3>" +
                "<p>訂單編號: " + order.getOrderId() + "</p>" +
                "<p>訂單狀態: " + status + "</p>" +
                "<p>我們將持續為您服務，謝謝您的耐心等待。</p>";
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitOrder(@RequestBody Map<String, Object> requestBody) {
        try {
            if (!requestBody.containsKey("cartId") || !requestBody.containsKey("member")
                    || !requestBody.containsKey("creditCard") || !requestBody.containsKey("shippingAddress")
                    || !requestBody.containsKey("selectedItems")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("提交失敗: 缺少必要參數 (cartId, member, creditCard, shippingAddress, selectedItems)");
            }

            int cartId = Integer.parseInt(requestBody.get("cartId").toString());
            int memberId = Integer.parseInt(requestBody.get("member").toString());
            String creditCard = requestBody.get("creditCard").toString();
            String shippingAddress = requestBody.get("shippingAddress").toString();

            Object itemsObj = requestBody.get("selectedItems");
            if (!(itemsObj instanceof List)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("提交失敗: selectedItems 應為列表");
            }

            List<?> selectedItemsRaw = (List<?>) itemsObj;
            List<Integer> selectedItems = selectedItemsRaw.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<?, ?>) item)
                    .filter(map -> map.containsKey("productId"))
                    .map(map -> Integer.parseInt(map.get("productId").toString()))
                    .collect(Collectors.toList());

            if (selectedItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("提交失敗: 必須選擇至少一個商品");
            }
            if (creditCard.trim().isEmpty() || shippingAddress.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("提交失敗: 信用卡資訊或送貨地址不能為空");
            }

            boolean isSubmitted = orderService.submitOrder(cartId, memberId, creditCard, shippingAddress,
                    selectedItems);

            return isSubmitted ? ResponseEntity.ok("訂單已成功提交")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法提交訂單: 訂單處理失敗");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("提交訂單時發生錯誤: " + e.getMessage());
        }
    }
}
