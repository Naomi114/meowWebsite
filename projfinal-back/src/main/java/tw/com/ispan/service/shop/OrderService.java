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
import tw.com.ispan.repository.shop.OrderRequest; // Ensure this import is correct

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
        order.setCreditCard(orderRequest.getCreditCard());
        order.setOrderStatus("待支付");
        order.setOrderDate(LocalDateTime.now());

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

        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    /**
     * Process payment for an order
     *
     * @param orderId The order ID
     * @param paymentRequest The payment request
     * @return The updated Orders object
     */
    @Transactional
    public Orders processPayment(Integer orderId, PaymentRequest paymentRequest) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    
        if ("success".equals(paymentRequest.getPaymentStatus())) {
            orders.setOrderStatus("已付款");
        } else {
            orders.setOrderStatus("付款失敗");
        }
    
        return orderRepository.save(orders);  // Return Orders object, not Sort.Order
    }

    /**
     * Update the order status
     *
     * @param orderId The order ID
     * @param status The new status
     * @return The updated Orders object
     */
    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("訂單未找到"));

        order.setOrderStatus(status);  // Corrected method to set order status

        return orderRepository.save(order);
    }

    /**
     * Clear the cart for the given order
     *
     * @param orderId The order ID
     */
    @Transactional
    public void clearCartForOrder(Integer orderId) {
        // Make sure you're using the result of findByOrderId as a List<CartItem>
        List<CartItem> cartItems = cartItemRepository.findByOrder_OrderId(orderId);
        
        // Now delete all items in the cart
        cartItemRepository.deleteAll(cartItems);
    }
}
