package tw.com.ispan.repository.shop;

import tw.com.ispan.domain.admin.Member;

public class OrderRequest {

    // Fields from the first OrderRequest
    private int productId;
    private Integer quantity;
    private String customerName;
    private String shippingAddress;

    // Fields from the second OrderRequest
    private Member memberId;
    private String creditCard;
    private int cartId;

    // Getters and Setters for the first OrderRequest
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters for the second OrderRequest
    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
