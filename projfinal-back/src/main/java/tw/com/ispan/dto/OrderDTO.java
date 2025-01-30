package tw.com.ispan.dto;

import java.util.List;

public class OrderDTO {
    private Integer id;  // 訂單 ID
    private String orderStatus;  // 訂單狀態
    private Double finalPrice;  // 最終價格
    private List<OrderItemDTO> orderItems;  // 訂單項目

    // Getters and Setters
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
}
