package tw.com.ispan.domain.shop;

import jakarta.persistence.*;
import tw.com.ispan.domain.admin.Member;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cart")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @OneToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(name = "lastUpdatedDate", nullable = false)
    private LocalDateTime lastUpdatedDate;

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

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}