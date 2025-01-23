package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.PaymentRequest;
import tw.com.ispan.repository.shop.CartItemRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;
import tw.com.ispan.repository.shop.OrderRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.repository.shop.OrderRequest;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * Create an order
     *
     * @param orderRequest The order request
     * @return The created Orders object
     */
    @Transactional
    public Orders createOrder(OrderRequest orderRequest) {
        Orders order = new Orders();
        order.setMemberId(orderRequest.getMemberId());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setCreditCard(orderRequest.getCreditCard()); // Ensure you handle encryption for credit card data
        order.setOrderStatus("待支付"); // Initial status as "Pending Payment"
        order.setOrderDate(LocalDateTime.now());

        // Calculate total price
        double totalPrice = 0;
        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(orderRequest.getCartId());
        for (CartItem item : cartItems) {
            BigDecimal itemPrice = item.getProduct().getPrice();
            BigDecimal totalItemPrice = itemPrice.multiply(new BigDecimal(item.getQuantity()));
            totalPrice += totalItemPrice.doubleValue();
        }
        order.setSubtotalPrice(totalPrice);
        order.setFinalPrice(totalPrice);

        Orders savedOrder = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getOrderId());
            orderItem.setProductId(item.getProduct().getProductId());
            orderItem.setOrderQuantity(item.getQuantity());
            orderItem.setPurchasedPrice(item.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }

        // Clear cart items only after the order is successfully saved
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    /**
     * Process payment for an order
     *
     * @param orderId        The order ID
     * @param paymentRequest The payment request
     * @return The updated Orders object
     */
    @Transactional
    public Orders processPayment(Integer orderId, PaymentRequest paymentRequest) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check payment status and update order status
        if ("success".equals(paymentRequest.getPaymentStatus())) {
            order.setOrderStatus("已付款"); // Paid
        } else {
            order.setOrderStatus("付款失敗"); // Payment failed
            // You may consider throwing an exception or logging the error here
        }

        return orderRepository.save(order);
    }

    /**
     * Update the order status
     *
     * @param orderId The order ID
     * @param status  The new status
     * @return The updated Orders object
     */
    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("訂單未找到"));

        order.setOrderStatus(status); // Corrected method to set order status

        return orderRepository.save(order);
    }

    /**
     * Clear the cart for the given order
     *
     * @param orderId The order ID
     */
    @Transactional
    public void clearCartForOrder(Integer orderId) {
        // Ensure you're using the correct cart items for the given order
        List<CartItem> cartItems = cartItemRepository.findByOrder_OrderId(orderId);

        // Now delete all items in the cart
        cartItemRepository.deleteAll(cartItems);
    }

    public Orders getOrderById(int orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderById'");
    }
}
