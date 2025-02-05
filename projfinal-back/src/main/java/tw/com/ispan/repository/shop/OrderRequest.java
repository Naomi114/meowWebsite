package tw.com.ispan.repository.shop;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.OrderItem;

public class OrderRequest {

    private Integer cartId; // Shopping cart ID to identify the cart

    @NotNull(message = "Member ID cannot be null")
    private Member member; // Member object for linking the member

    @NotNull(message = "Order items cannot be null")
    private List<OrderItem> items; // List of order items

    @NotNull(message = "Credit card information cannot be null")
    @Size(min = 16, max = 16, message = "Credit card number must be exactly 16 digits")
    private String creditCard; // Credit card information (16 digits)

    @NotNull(message = "Shipping address cannot be null")
    private String shippingAddress; // Shipping address

    @NotNull(message = "Transaction ID cannot be null")
    private String transactionId; // Transaction ID

    @NotNull(message = "Merchant Trade No cannot be null")
    private String merchantTradeNo; // Merchant Trade No

    // Default constructor
    public OrderRequest() {
    }

    // Getters and Setters
    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMerchantTradeNo() {
        return merchantTradeNo;
    }

    public void setMerchantTradeNo(String merchantTradeNo) {
        this.merchantTradeNo = merchantTradeNo;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "cartId=" + cartId +
                ", member=" + member +
                ", items=" + items +
                ", creditCard='[REDACTED]'" + // Don't print credit card number
                ", shippingAddress='" + shippingAddress + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", merchantTradeNo='" + merchantTradeNo + '\'' +
                '}';
    }

    public int getMemberId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemberId'");
    }

    public void setMemberId(Integer memberId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMemberId'");
    }
}
