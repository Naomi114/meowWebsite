package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProductImage")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isPrimary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false) // 確保不能為 NULL
    @JoinColumn(name = "FK_productId", foreignKey = @ForeignKey(name = "fkc_product_id"))
    @JsonManagedReference("productImages")
    private Product product;

    public ProductImage() {
    }

    public ProductImage(Integer imageId, String imageUrl, Boolean isPrimary, LocalDateTime createdAt,
            Product product) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductImageBean [imageId=" + imageId + ", imageUrl=" + imageUrl + ", isPrimary=" + isPrimary
                + ", createdAt=" + createdAt + ", product=" + product + "]";
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
