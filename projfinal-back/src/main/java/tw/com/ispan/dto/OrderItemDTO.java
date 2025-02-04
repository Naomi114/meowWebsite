package tw.com.ispan.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer productId;  // 產品 ID
    private Integer orderQuantity;  // 訂單數量
    private BigDecimal purchasedPrice;  // 購買價格

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(BigDecimal purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }
}
