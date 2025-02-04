package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.domain.admin.Admin;

@Entity
@Table(name = "Discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountId;

    // Many-to-one relationship with Admin
    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    @JsonBackReference("discounts") // Back reference for admin
    private Admin admin;

    @Column(nullable = false)
    private LocalDateTime discountStartTime;

    // One-to-many relationship with CartItem
    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cart-items-discount") // Managed side of the relationship
    private List<CartItem> cartItems;

    @Column(nullable = false)
    private LocalDateTime discountEndTime;

    @Column
    private Double minAmount; // Nullable by default

    @Column
    private Integer minQuantity; // Nullable by default

    @Column
    private Integer priority; // Nullable by default

    @Column(length = 20)
    private String discountStatus; // Nullable by default, can add length constraint

    @Column(length = 20)
    private String discountType; // Nullable by default, can add length constraint

    @Column
    private Double discountValue; // Nullable by default

    // One-to-many relationship with DiscountScope
    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("discount-scopes") // Managed side of the relationship
    private List<DiscountScope> discountScopes;

    // Constructors
    public Discount() {
    }

    public Discount(Integer discountId, Admin admin, LocalDateTime discountStartTime, LocalDateTime discountEndTime,
            Double minAmount, Integer minQuantity, Integer priority, String discountStatus,
            String discountType, Double discountValue, List<CartItem> cartItems, List<DiscountScope> discountScopes) {
        this.discountId = discountId;
        this.admin = admin;
        this.discountStartTime = discountStartTime;
        this.discountEndTime = discountEndTime;
        this.minAmount = minAmount;
        this.minQuantity = minQuantity;
        this.priority = priority;
        this.discountStatus = discountStatus;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.cartItems = cartItems;
        this.discountScopes = discountScopes;
    }

    // Getters and Setters
    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public LocalDateTime getDiscountStartTime() {
        return discountStartTime;
    }

    public void setDiscountStartTime(LocalDateTime discountStartTime) {
        this.discountStartTime = discountStartTime;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public LocalDateTime getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(LocalDateTime discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDiscountStatus() {
        return discountStatus;
    }

    public void setDiscountStatus(String discountStatus) {
        this.discountStatus = discountStatus;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public List<DiscountScope> getDiscountScopes() {
        return discountScopes;
    }

    public void setDiscountScopes(List<DiscountScope> discountScopes) {
        this.discountScopes = discountScopes;
    }

    @Override
    public String toString() {
        return "Discount [discountId=" + discountId + ", admin=" + admin + ", discountStartTime=" + discountStartTime
                + ", discountEndTime=" + discountEndTime + ", minAmount=" + minAmount + ", minQuantity=" + minQuantity
                + ", priority=" + priority + ", discountStatus=" + discountStatus + ", discountType=" + discountType
                + ", discountValue=" + discountValue + "]";
    }
}
