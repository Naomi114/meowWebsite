package tw.com.ispan.domain.shop;

import jakarta.persistence.*;
import tw.com.ispan.domain.admin.Member;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cart")
@JsonIgnoreProperties({ "cart", "member" })
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private Integer cartId; // This is the primary key field

    @OneToOne // One member can only have one cart
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // The member related to this cart

    @Column(name = "lastUpdatedDate", nullable = false)
    private LocalDateTime lastUpdatedDate; // Timestamp of the last update

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems; // List of cart items related to this cart

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

    public void setMember(Member member2) {
        this.member = member2;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
