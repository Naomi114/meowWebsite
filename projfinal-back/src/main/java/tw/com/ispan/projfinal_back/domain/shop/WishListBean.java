package tw.com.ispan.projfinal_back.domain.shop;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.projfinal_back.domain.admin.Member;

@Entity
@Table(name = "wishlist")
public class WishListBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishlistId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductBean product;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    // Getters and Setters
}

