package tw.com.ispan.projfinal_back.domain.shop;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.projfinal_back.domain.admin.Member;

@Entity
@Table(name = "Order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "discountId", nullable = false)
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

    // Constructors, getters, setters, toString()
}
