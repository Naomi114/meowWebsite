package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.OrderRequest;
import tw.com.ispan.dto.PaymentRequest;
import tw.com.ispan.repository.shop.CartItemRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;
import tw.com.ispan.repository.shop.OrderRepository;
import tw.com.ispan.repository.shop.ProductRepository;

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

    // 建立訂單
    @Transactional
    public Orders createOrder(OrderRequest orderRequest) {
        Orders order = new Orders();
        order.setMemberId(orderRequest.getMemberId());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setCreditCard(orderRequest.getCreditCard());
        order.setOrderStatus("待支付");
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0;
        List<CartItem> cartItems = cartItemRepository.findByCartId(orderRequest.getCartId());
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

    // 處理付款
    @Transactional
    public Orders processPayment(int orderId, PaymentRequest paymentRequest) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("success".equals(paymentRequest.getPaymentStatus())) {
            orders.setOrderStatus("已付款");
        } else {
            orders.setOrderStatus("付款失敗");
        }

        return orderRepository.save(orders);
    }

    // 更新訂單狀態
    @Transactional
    public Orders updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(Integer.valueOf(orderId))
                .orElseThrow(() -> new RuntimeException("訂單未找到"));
        order.setOrderStatus(status);
        return orderRepository.save(orderId);
    }

    // 清空購物車
    @Transactional
    public void clearCartForOrder(String orderId) {
        List<CartItem> cartItems = cartItemRepository.findByOrderId(Long.valueOf(orderId));
        cartItemRepository.deleteAll(cartItems);
    }
}
