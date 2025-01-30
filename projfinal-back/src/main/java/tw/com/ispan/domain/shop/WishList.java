package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "Wishlist")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishlistId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    @JsonManagedReference("wishlists")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @JsonManagedReference("wishlists")
    private Product product;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    public WishList() {
    }

    public WishList(Integer wishlistId, Member member, Product product, LocalDateTime addedAt) {
        this.wishlistId = wishlistId;
        this.member = member;
        this.product = product;
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "WishListBean [wishlistId=" + wishlistId + ", member=" + member + ", product=" + product
                + ", addedAt="
                + addedAt + "]";
    }

    public Integer getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
