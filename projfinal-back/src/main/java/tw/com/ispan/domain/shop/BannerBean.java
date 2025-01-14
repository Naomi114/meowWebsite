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
public class BannerBean {
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

    public BannerBean() {
    }

    public BannerBean(Integer bannerId, String bannerTitle, String bannerImageUrl, Date startDate, Date endDate,
            LocalDateTime createdAt, Discount discount) {
        this.bannerId = bannerId;
        this.bannerTitle = bannerTitle;
        this.bannerImageUrl = bannerImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "BannerBean [bannerId=" + bannerId + ", bannerTitle=" + bannerTitle + ", bannerImageUrl="
                + bannerImageUrl + ", startDate=" + startDate + ", endDate=" + endDate + ", createdAt=" + createdAt
                + ", discount=" + discount + "]";
    }

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

}
