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

    // 创建订单
    @Transactional
    public Orders createOrder(OrderRequest orderRequest) {
        Orders order = new Orders();
        order.setMember(orderRequest.getMember());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setCreditCard(orderRequest.getCreditCard());
        order.setOrderStatus("待支付"); // 初始状态: 待支付
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0;
        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(orderRequest.getCartId());
        for (CartItem item : cartItems) {
            BigDecimal itemPrice = item.getProduct().getSalePrice(); // 获取产品价格
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
            orderItem.setStatus("待出貨"); // 默认状态
            orderItemRepository.save(orderItem);
        }

        // 清空购物车项
        clearCartForOrder(orderRequest.getCartId());

        return savedOrder;
    }

    // 处理支付
    @Transactional
    public Orders processPayment(Integer orderId, PaymentRequest paymentRequest) {
        // Retrieve the order by ID with proper exception handling
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if ("success".equals(paymentRequest.getPaymentStatus())) {
            order.setOrderStatus("已付款");
        } else {
            order.setOrderStatus("付款失敗");
        }

        return orderRepository.save(order);
    }

    // 提交订单
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
                BigDecimal itemPrice = item.getProduct().getSalePrice(); // 获取产品价格
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

    // 更新订单状态
    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) {
        // Add better error handling for invalid orderId
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单未找到: " + orderId));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    // 获取订单信息
    public Orders getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单未找到: " + orderId));
    }

    // 获取所有订单
    public List<Orders> getAllOrders() {
        try {
            return (List<Orders>) orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("查询所有订单时发生错误", e);
        }
    }

    // 取消订单
    @Transactional
    public boolean cancelOrder(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getOrderStatus().equals("待支付")) {
            order.setOrderStatus("已取消");
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    // 清空购物车
    @Transactional
    public void clearCartForOrder(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getCartItems()); // 删除购物车中的所有商品
    }

    // 将订单转换为 DTO
    @Transactional
    public OrderDTO getOrderDTOById(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 确保初始化所有懒加载的属性
        order.getOrderItems().size();

        return convertToDTO(order); // 将订单数据转换为 DTO
    }

    private OrderDTO convertToDTO(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getOrderId());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setFinalPrice(order.getFinalPrice());

        // 转换订单项
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

    // 根据订单 ID 获取订单项
    public List<OrderItemDTO> getOrderItemsByOrderId(Integer orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(orderId);
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setProductId(orderItem.getProduct().getProductId());
            dto.setOrderQuantity(orderItem.getOrderQuantity());
            dto.setPurchasedPrice(orderItem.getPurchasedPrice());
            orderItemDTOs.add(dto);
        }
        return orderItemDTOs;
    }

    // 更新订单项状态
    public void updateOrderItemStatus(Integer orderId, Integer productId, String status) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderIdAndProduct_ProductId(orderId, productId);
        for (OrderItem item : orderItems) {
            item.setStatus(status);
            orderItemRepository.save(item);
        }
    }

    // 新方法：更新ECPay的transactionId
    @Transactional
    public Orders updateEcpayTransactionId(Integer orderId, String transactionId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 更新transactionId字段
        order.setTransactionId(transactionId);

        // 保存订单并返回
        return orderRepository.save(order);
    }

    // 原始方法：为 Long 类型的 orderId 提供获取订单信息的支持
    public Orders getOrderDTOById(Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderDTOById'");
    }
}
