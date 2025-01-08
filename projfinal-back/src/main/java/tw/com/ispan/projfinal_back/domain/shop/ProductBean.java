package tw.com.ispan.projfinal_back.domain.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.projfinal_back.domain.admin.Admin;

@Entity
@Table(name = "product")
public class ProductBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false, unique = true)
    private String productName;

    private String productDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productOriginalPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productSalePrice;

    @Column(nullable = false)
    private Integer productStock;

    private String productUnit;

    private String productStatus;

    @Column(nullable = false)
    private Date expire;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private ProductCategoryBean productCategory;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageBean> productImages;

    @ManyToMany
    @JoinTable(
        name = "product_tag",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagBean> tags = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAuditBean> stockAudits;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListBean> wishlists;

    public ProductBean() {
    }

    public ProductBean(Integer productId, String productName, String productDescription,
            BigDecimal productOriginalPrice, BigDecimal productSalePrice, Integer productStock, String productUnit,
            String productStatus, Date expire, LocalDateTime createdAt, LocalDateTime updatedAt,
            ProductCategoryBean productCategory, Admin admin, List<ProductImageBean> productImages, Set<TagBean> tags,
            List<StockAuditBean> stockAudits, List<WishListBean> wishlists) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productOriginalPrice = productOriginalPrice;
        this.productSalePrice = productSalePrice;
        this.productStock = productStock;
        this.productUnit = productUnit;
        this.productStatus = productStatus;
        this.expire = expire;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productCategory = productCategory;
        this.admin = admin;
        this.productImages = productImages;
        this.tags = tags;
        this.stockAudits = stockAudits;
        this.wishlists = wishlists;
    }

    @Override
    public String toString() {
        return "ProductBean [productId=" + productId + ", productName=" + productName + ", productDescription="
                + productDescription + ", productOriginalPrice=" + productOriginalPrice + ", productSalePrice="
                + productSalePrice + ", productStock=" + productStock + ", productUnit=" + productUnit
                + ", productStatus=" + productStatus + ", expire=" + expire + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", productCategory=" + productCategory + ", admin=" + admin
                + ", productImages=" + productImages + ", tags=" + tags + ", stockAudits=" + stockAudits
                + ", wishlists=" + wishlists + "]";
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public void setProductOriginalPrice(BigDecimal productOriginalPrice) {
        this.productOriginalPrice = productOriginalPrice;
    }

    public BigDecimal getProductSalePrice() {
        return productSalePrice;
    }

    public void setProductSalePrice(BigDecimal productSalePrice) {
        this.productSalePrice = productSalePrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductCategoryBean getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryBean productCategory) {
        this.productCategory = productCategory;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<ProductImageBean> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageBean> productImages) {
        this.productImages = productImages;
    }

    public Set<TagBean> getTags() {
        return tags;
    }

    public void setTags(Set<TagBean> tags) {
        this.tags = tags;
    }

    public List<StockAuditBean> getStockAudits() {
        return stockAudits;
    }

    public void setStockAudits(List<StockAuditBean> stockAudits) {
        this.stockAudits = stockAudits;
    }

    public List<WishListBean> getWishlists() {
        return wishlists;
    }

    public void setWishlists(List<WishListBean> wishlists) {
        this.wishlists = wishlists;
    }

    
}
