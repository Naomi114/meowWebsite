package tw.com.ispan.dto.shop;

import java.util.List;
import tw.com.ispan.domain.shop.Orders;

public class OrderDTO {
    private Integer id; // Order ID
    private String orderStatus; // Order status
    private Double finalPrice; // Final price after discounts
    private List<OrderItemDTO> orderItems; // List of order items

    // Constructor: 将 Orders 对象转换为 OrderDTO
    public OrderDTO(Orders order) {
        this.id = order.getOrderId().intValue(); // 假设 OrderId 是 Long 类型，转换为 Integer
        this.orderStatus = order.getOrderStatus(); // 获取订单状态
        this.finalPrice = order.getFinalPrice(); // 获取最终价格
        // 假设 Orders 类有获取订单项的方法，这里需要根据实际情况填充订单项
        // this.orderItems = mapOrderItems(order.getOrderItems());
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    // 假设有方法将订单项转换为 OrderItemDTO 列表
    // private List<OrderItemDTO> mapOrderItems(List<OrderItem> orderItems) {
    // // 转换逻辑
    // return ...;
    // }
}
