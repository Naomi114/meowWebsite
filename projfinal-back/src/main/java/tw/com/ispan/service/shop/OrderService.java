package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.shop.OrderDTO;
import tw.com.ispan.dto.shop.OrderItemDTO;
import tw.com.ispan.dto.shop.OrderRequest;
import tw.com.ispan.dto.shop.PaymentRequest;
import tw.com.ispan.repository.shop.CartItemRepository;
import tw.com.ispan.repository.shop.CartRepository;
import tw.com.ispan.repository.shop.IOrderService;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;
import tw.com.ispan.repository.shop.OrderRepository;
import tw.com.ispan.repository.shop.ProductRepository;

@Service
public class OrderService implements IOrderService {

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

    @Autowired
    private JavaMailSender javaMailSender; // 用于发送邮件

    @Autowired
    private EmailService emailService; // 用于发送邮件的 EmailService

    private final Map<Integer, String> orderTradeMap = new HashMap<>();

    // 更新 ECPay 交易 ID 和 MerchantTradeNo
    @Transactional
    public Orders updateEcpayTransactionIds(Integer orderId, String merchantTradeNo, String transactionId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 更新 merchantTradeNo 和 transactionId 字段
        order.setMerchantTradeNo(merchantTradeNo);
        order.setTransactionId(transactionId);

        // 保存订单并返回
        return orderRepository.save(order);
    }

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

    // 更新订单状态并发送邮件
    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) throws MessagingException {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单未找到: " + orderId));

        // 更新訂單狀態
        order.setOrderStatus(status);
        Orders updatedOrder = orderRepository.save(order);

        // 如果狀態變更為「已支付」、「備貨中」或「出貨中」，則發送郵件
        if (status.equals("已支付") || status.equals("備貨中") || status.equals("出貨中")) {
            // 準備郵件內容
            String emailContent = generateEmailContent(updatedOrder, status);

            // 發送郵件並處理錯誤
            try {
                emailService.sendEmail(updatedOrder.getMember().getEmail(), "訂單狀態更新", emailContent);
            } catch (Exception e) {
                // 打印出錯信息以便調試
                e.printStackTrace();
                throw new MessagingException("郵件發送失敗: " + e.getMessage());
            }
        }

        return updatedOrder;
    }

    // 根據訂單和狀態生成郵件內容
    private String generateEmailContent(Orders order, String status) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<h3>您的訂單狀態已更新</h3>");
        emailContent.append("<p>訂單編號: " + order.getOrderId() + "</p>");
        emailContent.append("<p>訂單狀態: " + status + "</p>");

        if (status.equals("已支付")) {
            emailContent.append("<p>訂單總金額: NT$" + order.getFinalPrice() + "</p>");
            emailContent.append("<p>訂單詳情：</p>");
            emailContent.append("<table border='1'><tr><th>商品名稱</th><th>數量</th><th>單價</th><th>金額</th></tr>");

            // 生成訂單項目
            for (OrderItem item : order.getOrderItems()) {
                emailContent.append("<tr>")
                        .append("<td>").append(item.getProduct().getProductName()).append("</td>")
                        .append("<td>").append(item.getOrderQuantity()).append("</td>")
                        .append("<td>").append(item.getPurchasedPrice()).append("</td>")
                        .append("<td>")
                        .append(item.getPurchasedPrice().multiply(new BigDecimal(item.getOrderQuantity())))
                        .append("</td>")
                        .append("</tr>");
            }

            emailContent.append("</table>");
        } else {
            emailContent.append("<p>您的訂單正在處理中，將很快更新為下一步狀態。</p>");
        }

        return emailContent.toString();
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
        OrderDTO orderDTO = new OrderDTO(order);
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

    // 获取所有订单 DTO
    public List<OrderDTO> getAllOrderDTOs() {
        List<Orders> orders = getAllOrders();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Orders order : orders) {
            orderDTOs.add(convertToDTO(order));
        }
        return orderDTOs;
    }

    // 根据 ECPay 交易 ID 获取订单 ID
    @SuppressWarnings("rawtypes")
    @Override
    public Optional getOrderIdByEcpayTransactionId(String ecpayTransactionId) {
        // 获取与 ECPay 交易ID 相关的订单 ID
        Orders orders = orderRepository.findByTransactionId(ecpayTransactionId);
        return Optional.empty();
    }

    @Override
    public void updateEcpayTransactionId(Integer orderId, String merchantTradeNo) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setMerchantTradeNo(merchantTradeNo);
        orderRepository.save(order); // 保存订单
    }

    @Override
    public Orders getOrderDTOById(Long orderId) {
        Orders order = orderRepository.findById(orderId.intValue())
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return order;

    }

    @Override
    public Optional<Orders> getOrderByEcpayTransactionId(String transactionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderByEcpayTransactionId'");
    }

    @Override
    public Optional<Orders> getOrderByMerchantTradeNo(String merchantTradeNo) {
        Orders order = orderRepository.findByMerchantTradeNo(merchantTradeNo);
        return Optional.ofNullable(order);
    }

    @Transactional
    public void updateMerchantTradeNo(Integer orderId, String merchantTradeNo) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setMerchantTradeNo(merchantTradeNo);
        orderRepository.save(order); // Save the updated order
    }

    @Transactional
public boolean submitOrder(int cartId, int memberId, String creditCard, String shippingAddress,
        List<Integer> selectedItems) {
    try {
        // 确保 selectedItems 不是 null
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new RuntimeException("未選擇商品");
        }

        // 查询购物车
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("找不到購物車"));

        // 查询用户
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        // 确保购物车属于该会员
        if (cart.getMember().getMemberId() != memberId) {
            throw new RuntimeException("購物車與會員ID不匹配");
        }

        // 过滤出被选中的商品
        List<CartItem> cartItems = cart.getCartItems().stream()
                .filter(item -> selectedItems.contains(item.getProduct().getProductId())) // 只保留被选中的商品
                .toList();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("未選擇有效的商品");
        }

        // 计算订单总金额
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getSalePrice().doubleValue() * item.getQuantity())
                .sum();

        // 创建订单
        Orders newOrder = new Orders();
        newOrder.setMember(member);
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setCreditCard(creditCard);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setOrderStatus("待支付");
        newOrder.setSubtotalPrice(totalPrice);
        newOrder.setFinalPrice(totalPrice);

        Orders savedOrder = orderRepository.save(newOrder);

        // 添加订单项
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(item.getProduct());
            orderItem.setOrderQuantity(item.getQuantity());
            orderItem.setPurchasedPrice(item.getProduct().getSalePrice());
            orderItem.setStatus("待出貨");
            orderItemRepository.save(orderItem);
        }

        // 从购物车中删除已选中的商品
        cartItemRepository.deleteAll(cartItems);

        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public Orders processPayment(int orderId, PaymentRequest paymentRequest) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
}

public List<Orders> getOrdersByMemberId(int memberId) {
    try {
        // Fetch orders by memberId directly, referencing member's memberId
        return orderRepository.findByMember_MemberId(memberId);
    } catch (Exception e) {
        // Handle the exception and throw a RuntimeException with the error message
        throw new RuntimeException("Error fetching orders: " + e.getMessage(), e);
    }
}
public void submitOrder(Map<String, Object> requestBody, int memberId, Cart cart) {
    // 這裡處理提交訂單邏輯
    // 可以利用傳入的 memberId 和 cart 來處理訂單相關的操作

    // 範例：假設根據 memberId 和 cartId 進行訂單創建
    System.out.println("Submitting order for member ID: " + memberId);
    System.out.println("Cart details: " + cart);
}
}