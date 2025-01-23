package tw.com.ispan.dto;

import tw.com.ispan.domain.shop.CartItem;

import java.math.BigDecimal;

public class CartItemResponse {

    private Integer cartItemId;
    private String productName;
    private BigDecimal salePrice;
    private Integer cartItemQuantity;

    // Constructor to populate from CartItem entity
    public CartItemResponse(CartItem cartItem) {
        this.cartItemId = cartItem.getCartItemId();
        this.productName = cartItem.getProduct().getProductName(); // Accessing productName from the Product entity
        this.salePrice = cartItem.getProduct().getSalePrice(); // Accessing salePrice from the Product entity
        this.cartItemQuantity = cartItem.getCartItemQuantity();
    }

    // Getters and Setters
    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }
}
