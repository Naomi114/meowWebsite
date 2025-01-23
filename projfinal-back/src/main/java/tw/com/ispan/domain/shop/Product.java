package tw.com.ispan.domain.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // Total 10 digits: 8 integer digits and 2 decimal digits
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

    // LocalDate stores year, month, day
    @Column(nullable = false)
    private LocalDate expire;

    // LocalDateTime stores year, month, day, hour, minute, second
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Many-to-one relationship with Category (bi-directional)
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "FK_categoryId", foreignKey = @ForeignKey(name = "fkc_category_id"))
    @JsonBackReference("products")
    private Category category;

    // Many-to-one relationship with Admin (bi-directional)
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "FK_adminId", foreignKey = @ForeignKey(name = "fkc_admin_id"))
    @JsonBackReference("products")
    private Admin admin;

    // One-to-many relationship with ProductImage (bi-directional)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonBackReference("product")
    private List<ProductImage> productImages = new LinkedList<>(); // Ordered and duplicates allowed (first image is
                                                                   // selected)

    // Many-to-many relationship with ProductTag (bi-directional)
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinTable(name = "Product_tag", joinColumns = @JoinColumn(name = "productid"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonBackReference("products")
    private Set<ProductTag> tags = new LinkedHashSet<>(); // Ordered, no duplicates

    // One-to-many relationship with InventoryItem (unidirectional)
    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<InventoryItem> inventoryItems = new LinkedList<>(); // Unordered, duplicates allowed (frequent
                                                                     // insertion and deletion)

    // One-to-many relationship with WishList (bi-directional)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonBackReference("products")
    private Set<WishList> wishlists = new LinkedHashSet<>(); // Ordered, no duplicates

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
        return "Product [productId=" + productId + ", productName=" + productName + ", description=" + description
                + ", originalPrice=" + originalPrice + ", salePrice=" + salePrice + ", stockQuantity=" + stockQuantity
                + ", unit=" + unit + ", status=" + status + ", expire=" + expire + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", category=" + category + ", admin=" + admin + ", productImages="
                + productImages + ", tags=" + tags + ", inventoryItems=" + inventoryItems + ", wishlists=" + wishlists
                + "]";
    }

    // Getter for the sale price
    public BigDecimal getPrice() {
        return salePrice; // You can return either originalPrice or salePrice based on your logic
    }

    // Getters and Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public Set<ProductTag> getTags() {
        return tags;
    }

    public void setTags(Set<ProductTag> tags) {
        this.tags = tags;
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public Set<WishList> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<WishList> wishlists) {
        this.wishlists = wishlists;
    }
}
