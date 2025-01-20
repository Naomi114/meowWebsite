package tw.com.ispan.domain.shop;

import jakarta.persistence.*;
import tw.com.ispan.domain.admin.Member;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart") // Database table name (lowercase to maintain consistency with standard naming
                      // conventions)
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId") // Explicitly specify column name in database
    private Long cartId; // Primary key, Long type

    @ManyToOne // One member can have multiple cart items
    @JoinColumn(name = "memberId", nullable = false) // Foreign key linking to the Member entity
    private Member member;

    @Column(name = "lastUpdatedDate", nullable = false) // Date when the cart was last updated
    private LocalDateTime lastUpdatedDate;

    @Column(name = "productId", nullable = false) // Product ID
    private Integer productId;

    @Column(name = "productName", nullable = false) // Product name
    private String productName;

    @Column(name = "salePrice", nullable = false) // Sale price of the product
    private Double salePrice;

    @Column(name = "quantity", nullable = false) // Quantity of the product in the cart
    private Integer quantity;

    @Transient // Indicates that this field should not be stored in the database
    private Boolean selected; // Whether the item is selected (for front-end display)

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
