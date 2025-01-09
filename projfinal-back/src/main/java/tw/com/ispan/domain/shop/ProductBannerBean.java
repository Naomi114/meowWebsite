package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "banner")
public class ProductBannerBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bannerId;

    @Column(nullable = false)
    private String bannerTitle;

    @Column(nullable = false)
    private String bannerImageUrl;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "discountId", nullable = false)
    private Discount discount;

    // Getters and Setters
}
