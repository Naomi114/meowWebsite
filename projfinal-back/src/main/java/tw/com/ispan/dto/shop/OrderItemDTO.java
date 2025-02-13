package tw.com.ispan.dto.shop;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer productId; // 產品 ID
    private Integer orderQuantity; // 訂單數量
    private BigDecimal purchasedPrice; // 購買價格
    private String productName; // 商品名稱

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

    // 正確返回商品名稱
    public String getProductName() {
        return productName; // 返回商品名稱
    }

    public void setProductName(String productName) {
        this.productName = productName; // 設置商品名稱
    }
}
