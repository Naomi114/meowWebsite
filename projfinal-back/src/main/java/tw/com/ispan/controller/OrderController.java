package tw.com.ispan.controller;

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

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create an order
     *
     * @param cartId       The cart ID
     * @param orderRequest The order request
     * @return The created Orders object
     */
    @PostMapping("/create/{cartId}")
    public ResponseEntity<Orders> createOrder(@PathVariable int cartId, @RequestBody OrderRequest orderRequest) {
        try {
            orderRequest.setCartId(cartId); // Set the cartId from path variable
            Orders createdOrder = orderService.createOrder(orderRequest); // Call the service to create the order
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder); // Return the created order
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 error if something goes wrong
        }
    }

    /**
     * Process payment for an order
     *
     * @param orderId        The order ID
     * @param paymentRequest The payment request
     * @return The updated Orders object
     */
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Orders> processPayment(@PathVariable int orderId,
            @RequestBody PaymentRequest paymentRequest) {
        try {
            Orders updatedOrder = orderService.processPayment(orderId, paymentRequest); // Call the service to process
                                                                                        // payment
            return ResponseEntity.ok(updatedOrder); // Return the updated order after payment
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 error if something goes wrong
        }
    }

    /**
     * Get order details by ID
     *
     * @param orderId The order ID
     * @return The OrderDTO object containing order details
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable int orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrderDTOById(orderId); // Get the DTO from service
            return ResponseEntity.ok(orderDTO); // Return the DTO
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 error if something goes wrong
        }
    }

    /**
     * Get all orders
     *
     * @return List of all Orders
     */
    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        try {
            List<Orders> orders = orderService.getAllOrders(); // Fetch all orders from the service
            return ResponseEntity.ok(orders); // Return the list of orders
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 error if something goes wrong
        }
    }

    /**
     * Cancel an order
     *
     * @param orderId The order ID
     * @return A message indicating whether the order was successfully canceled
     */
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam int orderId) {
        try {
            boolean isCancelled = orderService.cancelOrder(orderId); // Call the service to cancel the order
            if (isCancelled) {
                return ResponseEntity.ok("訂單已成功取消"); // Return success message
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法取消訂單"); // Return failure message
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("取消訂單時發生錯誤"); // Return error message if exception occurs
        }
    }

    /**
     * Submit an order (new endpoint added)
     *
     * @param requestBody The order submission details
     * @return A message indicating whether the order was successfully submitted
     */
    @PostMapping("/submit")
    public ResponseEntity<String> submitOrder(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract data from the request body
            int cartId = (int) requestBody.get("cartId");
            int memberId = (int) requestBody.get("member");
            String creditCard = (String) requestBody.get("creditCard");
            String shippingAddress = (String) requestBody.get("shippingAddress");

            // Call service to create new order
            boolean isSubmitted = orderService.submitOrder(cartId, memberId, creditCard, shippingAddress);

            if (isSubmitted) {
                return ResponseEntity.ok("訂單已成功提交");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法提交訂單");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("提交訂單時發生錯誤");
        }
    }
}
