package tw.com.ispan.dto;

import javax.validation.constraints.NotNull;

public class CartRequest {

    @NotNull(message = "Member ID cannot be null") // 加入非空校验
    private Integer memberId;

    @NotNull(message = "Product ID cannot be null") // 加入非空校验
    private Integer productId;

    @NotNull(message = "Quantity cannot be null") // 加入非空校验
    private Integer quantity;

    // Getters and Setters
    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
