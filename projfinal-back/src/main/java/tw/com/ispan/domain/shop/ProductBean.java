package tw.com.ispan.domain.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Admin;

@Entity
@Table(name = "product")
public class ProductBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = true)
    private String description;

    // 總共 10 位數，整數 8 位，小數 2 位
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = true)
    private String unit;

    @Column(nullable = true)
    private String status;

    @Column(nullable = false)
    private Date expire;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 雙向關係的多對一端，藉由DTO/ProductRequest 解決雙向序列化問題
    @ManyToOne
    @JoinColumn(name = "FK_categoryId", foreignKey = @ForeignKey(name = "fkc_category_id"))
    private CategoryBean category;

    // 雙向關係的多對一端；尚未檢查 Admin 實體的關聯????
    @ManyToOne
    @JoinColumn(name = "FK_adminId", foreignKey = @ForeignKey(name = "fkc_admin_id"))
    private Admin admin;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_imageId", foreignKey = @ForeignKey(name = "fkc_image_id"))
    private List<ProductImageBean> productImages;

    @ManyToMany
    @JoinTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagBean> tags = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAuditBean> stockAudits;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListBean> wishlists;

    public ProductBean() {
    }

    public ProductBean(Integer productId, String productName, String description, BigDecimal originalPrice,
            BigDecimal salePrice, Integer stockQuantity, String unit, String status, Date expire,
            LocalDateTime createdAt, LocalDateTime updatedAt, CategoryBean category, Admin admin,
            List<ProductImageBean> productImages, Set<TagBean> tags, List<StockAuditBean> stockAudits,
            List<WishListBean> wishlists) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.status = status;
        this.expire = expire;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.admin = admin;
        this.productImages = productImages;
        this.tags = tags;
        this.stockAudits = stockAudits;
        this.wishlists = wishlists;
    }

    @Override
    public String toString() {
        return "ProductBean [productId=" + productId + ", productName=" + productName + ", description=" + description
                + ", originalPrice=" + originalPrice + ", salePrice=" + salePrice + ", stockQuantity=" + stockQuantity
                + ", unit=" + unit + ", status=" + status + ", expire=" + expire + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", category=" + category + ", admin=" + admin
                + ", productImages=" + productImages + ", tags=" + tags + ", stockAudits=" + stockAudits
                + ", wishlists=" + wishlists + "]";
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getStatus() {
        return status;
    }

    public Date getExpire() {
        return expire;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public Admin getAdmin() {
        return admin;
    }

    public List<ProductImageBean> getProductImages() {
        return productImages;
    }

    public Set<TagBean> getTags() {
        return tags;
    }

    public List<StockAuditBean> getStockAudits() {
        return stockAudits;
    }

    public List<WishListBean> getWishlists() {
        return wishlists;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
        // 確保雙向關係一致: 若目前商品類別不包含此商品，則將此商品加入商品類別
        if (!category.getProducts().contains(this)) {
            category.getProducts().add(this);
        }
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setProductImages(List<ProductImageBean> productImages) {
        this.productImages = productImages;
    }

    public void setTags(Set<TagBean> tags) {
        this.tags = tags;
    }

    public void setStockAudits(List<StockAuditBean> stockAudits) {
        this.stockAudits = stockAudits;
    }

    public void setWishlists(List<WishListBean> wishlists) {
        this.wishlists = wishlists;
    }

}
