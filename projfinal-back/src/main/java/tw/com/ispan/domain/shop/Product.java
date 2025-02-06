package tw.com.ispan.domain.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Admin;

@Entity
@Table(name = "Product")
public class Product {
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

    // LocalDate 存年月日
    @Column(nullable = false)
    private LocalDate expire;

    // LocalDateTime 存年月日時分秒
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 雙向多對一，可反向查找
    // cascade = CascadeType.remove 會導致刪除商品時刪除商品類別；只有新增、修改、更新同步
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "FK_categoryId", foreignKey = @ForeignKey(name = "fkc_category_id"))
    @JsonBackReference("products")
    private Category category;

    // 雙向多對一，可反向查找
    // 尚待確認 Admin 表格有fetch = FetchType.EAGER (預設為 LAZY)
    // cascade = CascadeType.remove 會導致刪除商品時刪除管理員；須排除在外
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "FK_adminId", foreignKey = @ForeignKey(name = "fkc_admin_id"))
    @JsonBackReference("products")
    private Admin admin;

    // 雙向一對多，可反向查找
    // cascade = CascadeType.remove 當刪除商品時，會刪除商品圖片；已包含在 ALL 內
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference("product")
    private List<ProductImage> productImages = new LinkedList<>(); // 有序可重複 (首圖為選取的第一張)

    // 雙向多對多，可反向查找；可選0~N個標籤
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "Product_tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = true))
    @JsonBackReference("products")
    private Set<ProductTag> tags = new HashSet<>(); // 無序不重複

    // 單向一對多，可由商品查找庫存異動
    // 商品刪除時，保留相關的庫存異動記錄
    // cascade = CascadeType.remove 當刪除商品時，會刪除庫存異動；須排除在外
    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<InventoryItem> inventoryItems = new LinkedList<>();

    // 雙向一對多，可反向查找 (刪除願望清單，會員商品列表也會同步? 合理??)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonBackReference("products")
    private Set<WishList> wishlists = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference("cartItem")
    private CartItem cartItem;

    public Product() {
    }

    public Product(Integer productId, String productName, String description, BigDecimal originalPrice,
            BigDecimal salePrice, Integer stockQuantity, String unit, String status, LocalDate expire,
            LocalDateTime createdAt, LocalDateTime updatedAt, Category category, Admin admin,
            List<ProductImage> productImages, Set<ProductTag> tags,
            List<InventoryItem> inventoryItems, Set<WishList> wishlists) {
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
        this.inventoryItems = inventoryItems;
        this.wishlists = wishlists;
    }

    @Override
    public String toString() {
        return "ProductBean [productId=" + productId + ", productName=" + productName + ", description=" + description
                + ", originalPrice=" + originalPrice + ", salePrice=" + salePrice + ", stockQuantity=" + stockQuantity
                + ", unit=" + unit + ", status=" + status + ", expire=" + expire + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", category=" + category + ", adminId=" + admin + ", productImages="
                + productImages + ", tags=" + tags + ", inventoryItems=" + inventoryItems + ", wishlists=" + wishlists
                + "]";
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

    public LocalDate getExpire() {
        return expire;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public Admin getAdmin() {
        return admin;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public Set<ProductTag> getTags() {
        return tags;
    }

    public Set<WishList> getWishlists() {
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

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public void setTags(Set<ProductTag> tags) {
        this.tags = tags;
    }

    public void addTag(ProductTag tag) {
        this.tags.add(tag);
    }

    public void removeTag(ProductTag tag) {
        this.tags.remove(tag);
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void setWishlists(Set<WishList> wishlists) {
        this.wishlists = wishlists;
    }

    // 確保時間自動填入
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.expire == null) {
            this.expire = LocalDate.now().plusMonths(3); // 設定預設值為三個月後
        }
    }
}
