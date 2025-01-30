package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.OrderDTO;
import tw.com.ispan.dto.OrderItemDTO;
import tw.com.ispan.dto.PaymentRequest;
import tw.com.ispan.repository.shop.CartItemRepository;
import tw.com.ispan.repository.shop.CartRepository;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;
import tw.com.ispan.repository.shop.OrderRepository;
import tw.com.ispan.repository.shop.OrderRequest;
import tw.com.ispan.repository.shop.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Orders createOrder(OrderRequest orderRequest) {
        Orders order = new Orders();
        order.setMember(orderRequest.getMember());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setCreditCard(orderRequest.getCreditCard());
        order.setOrderStatus("待支付"); // Initial status: Pending Payment
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0;
        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(orderRequest.getCartId());
        for (CartItem item : cartItems) {
            BigDecimal itemPrice = item.getProduct().getSalePrice(); // getPrice => getSalePrice (by Naomi)
            BigDecimal totalItemPrice = itemPrice.multiply(new BigDecimal(item.getQuantity()));
            totalPrice += totalItemPrice.doubleValue();
        }
        order.setSubtotalPrice(totalPrice);
        order.setFinalPrice(totalPrice);

        Orders savedOrder = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(item.getProduct());
            orderItem.setOrderQuantity(item.getQuantity());
            orderItem.setPurchasedPrice(item.getProduct().getSalePrice());
            orderItem.setStatus("待出貨");  // Set default status
            orderItemRepository.save(orderItem);
        }

        // Clear the cart items after the order is created
        clearCartForOrder(orderRequest.getCartId());

        return savedOrder;
    }

    @Transactional
    public Orders processPayment(Integer orderId, PaymentRequest paymentRequest) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("success".equals(paymentRequest.getPaymentStatus())) {
            order.setOrderStatus("已付款");
        } else {
            order.setOrderStatus("付款失敗");
        }

        return orderRepository.save(order);
    }

    @Transactional
    public boolean submitOrder(int cartId, int memberId, String creditCard, String shippingAddress) {
        try {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            double totalPrice = 0;
            List<CartItem> cartItems = cart.getCartItems();
            for (CartItem item : cartItems) {
                BigDecimal itemPrice = item.getProduct().getSalePrice(); //getPrice => getSalePrice (by Naomi)
                BigDecimal totalItemPrice = itemPrice.multiply(new BigDecimal(item.getQuantity()));
                totalPrice += totalItemPrice.doubleValue();
            }

            Orders newOrder = new Orders();
            newOrder.setMember(member);
            newOrder.setShippingAddress(shippingAddress);
            newOrder.setCreditCard(creditCard);
            newOrder.setOrderDate(LocalDateTime.now());
            newOrder.setOrderStatus("待支付");
            newOrder.setSubtotalPrice(totalPrice);
            newOrder.setFinalPrice(totalPrice);

            Orders savedOrder = orderRepository.save(newOrder);

            for (CartItem item : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProduct(item.getProduct());
                orderItem.setOrderQuantity(item.getQuantity());
                orderItem.setPurchasedPrice(item.getProduct().getSalePrice());
                orderItem.setStatus("待出貨");
                orderItemRepository.save(orderItem);
            }

            cartItemRepository.deleteAll(cartItems);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("訂單未找到"));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    public Orders getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("訂單未找到"));
    }

    public List<Orders> getAllOrders() {
        try {
            return (List<Orders>) orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("查詢所有訂單時發生錯誤", e);
        }
    }

    @Transactional
    public boolean cancelOrder(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus().equals("待支付")) {
            order.setOrderStatus("已取消");
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    /**
     * Clears the cart after an order is created.
     *
     * @param cartId The cart ID to clear
     */
    @Transactional
    public void clearCartForOrder(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getCartItems()); // Deletes all items from the cart
    }

    /**
     * Convert Orders to OrderDTO
     */
    @Transactional
    public OrderDTO getOrderDTOById(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 確保初始化所有懶加載的屬性
        order.getOrderItems().size();

        return convertToDTO(order); // 將資料轉換為 DTO
    }

    private OrderDTO convertToDTO(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getOrderId());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setFinalPrice(order.getFinalPrice());

        // 轉換 orderItems
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setProductId(item.getProduct().getProductId());
            orderItemDTO.setOrderQuantity(item.getOrderQuantity());
            orderItemDTO.setPurchasedPrice(item.getPurchasedPrice());
            orderItemDTOs.add(orderItemDTO);
        }
        orderDTO.setOrderItems(orderItemDTOs);

        return orderDTO;
    }
}
