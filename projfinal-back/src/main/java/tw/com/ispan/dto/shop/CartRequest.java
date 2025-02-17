package tw.com.ispan.dto.shop;

import java.util.List;
import tw.com.ispan.domain.shop.CartItem;

public class CartRequest {
    private List<CartItem> items;

    private Integer memberId;

    private Integer productId;

    private Integer quantity;

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
