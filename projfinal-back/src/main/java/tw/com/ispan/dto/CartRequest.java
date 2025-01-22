package tw.com.ispan.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import tw.com.ispan.domain.shop.CartItem;

public class CartRequest {
    private List<CartItem> items;

    @NotNull(message = "Member ID cannot be null") // 加入非空校验
    private Integer memberId;

    @NotNull(message = "Product ID cannot be null") // 加入非空校验
    private Integer productId;

    @NotNull(message = "Quantity cannot be null") // 加入非空校验
    private Integer quantity;

    @NotNull(message = "Cart Item ID cannot be null") // 加入非空校验
    private Integer cartItemId;

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

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }
}
