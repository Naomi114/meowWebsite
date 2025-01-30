package tw.com.ispan.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer productId;  // 產品 ID
    private Integer orderQuantity;  // 訂單數量
    private Double purchasedPrice;  // 購買價格

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

    public Double getPurchasedPrice() {
        return purchasedPrice;
    }


    public void setPurchasedPrice(Double purchasedPrice2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPurchasedPrice'");
    }

    public void setPurchasedPrice(BigDecimal purchasedPrice2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPurchasedPrice'");
    }
}
