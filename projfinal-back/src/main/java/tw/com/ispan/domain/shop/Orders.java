package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = true)
    private Discount discount;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String creditCard;

    @Column(nullable = false)
    private String orderStatus;

    @Column
    private String feedback;

    @Column(nullable = false)
    private Double subtotalPrice;

    @Column(nullable = false)
    private Double finalPrice;

    public Orders() {
    }

    public Orders(Integer orderId, Member member, List<OrderItem> orderItems, Discount discount,
            String shippingAddress, LocalDateTime orderDate, String creditCard,
            String orderStatus, String feedback, Double subtotalPrice, Double finalPrice) {
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
    }

    @Override
    public String toString() {
        return "Orders [orderId=" + orderId + ", member=" + member + ", orderItems=" + orderItems +
                ", discount=" + discount + ", shippingAddress=" + shippingAddress + ", orderDate=" + orderDate +
                ", creditCard=" + creditCard + ", orderStatus=" + orderStatus + ", feedback=" + feedback +
                ", subtotalPrice=" + subtotalPrice + ", finalPrice=" + finalPrice + "]";
    }

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

    public void setMemberId(Member memberId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMemberId'");
    }
}
