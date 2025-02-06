package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "orders") // Ensure using table name "orders"
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId; // Order ID

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false) // Link to Member
    @JsonBackReference // Prevents infinite recursion when serializing
    private Member member; // Member

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference // Prevents infinite recursion when serializing
    private List<OrderItem> orderItems; // List of Order Items

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount; // Discount applied to the order

    @Column(nullable = false)
    private String shippingAddress; // Shipping Address

    @Column(nullable = false)
    private LocalDateTime orderDate; // Order Date

    @Column(nullable = false)
    private String creditCard; // Credit Card Information

    @Column(nullable = false)
    private String orderStatus; // Order Status (e.g., 'Pending', 'Shipped')

    @Column
    private String feedback; // Customer Feedback

    @Column(nullable = false)
    private Double subtotalPrice; // Subtotal Price before discount

    @Column(nullable = false)
    private Double finalPrice; // Final Price after applying discount

    @Column(nullable = true) // Allow null for transactionId
    private String transactionId; // Transaction ID (for looking up orders)

    @Column(nullable = true) // Allow null for merchantTradeNo
    private String merchantTradeNo; // Merchant Trade No (for looking up orders)

    // Default Constructor
    public Orders() {
    }

    // Constructor with all fields
    public Orders(Integer orderId, Member member, List<OrderItem> orderItems, Discount discount,
            String shippingAddress, LocalDateTime orderDate, String creditCard,
            String orderStatus, String feedback, Double subtotalPrice, Double finalPrice,
            String transactionId, String merchantTradeNo) {
        this.orderId = orderId;
        this.member = member;
        this.orderItems = orderItems;
        this.discount = discount;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.creditCard = creditCard;
        this.orderStatus = orderStatus;
        this.feedback = feedback;
        this.subtotalPrice = subtotalPrice;
        this.finalPrice = finalPrice;
        this.transactionId = transactionId;
        this.merchantTradeNo = merchantTradeNo;
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Double getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(Double subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
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

    // Optional method, though it is not clear what it's intended to do.
    // You can remove it if not necessary, or clarify the intent.
    public Orders orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }

    public String getCustomerEmail() {
        return this.member.getEmail(); // Assuming 'member' has a method 'getEmail()'
    }
}
