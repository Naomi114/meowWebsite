package tw.com.ispan.repository.shop;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderRequest {

    // Fields related to the order
    private Integer cartId;           // Cart ID (to identify the cart)
    
    @NotNull(message = "Member ID cannot be null")
    private Integer member;           // Member ID (to link to the Member)
    
    @NotNull(message = "Credit card information cannot be null")
    @Size(min = 16, max = 16, message = "Credit card number must be exactly 16 digits")
    private String creditCard;        // Credit card information (16 digits)
    
    @NotNull(message = "Shipping address cannot be null")
    private String shippingAddress;   // Shipping address

    // Default constructor
    public OrderRequest() {}

    // Getters and Setters
    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getMember() {
        return member;
    }

    public void setMember(Integer member) {
        this.member = member;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "cartId=" + cartId +
                ", member=" + member +
                ", creditCard='[REDACTED]'" +  // Don't print credit card in logs
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
