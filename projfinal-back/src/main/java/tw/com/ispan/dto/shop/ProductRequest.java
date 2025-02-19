package tw.com.ispan.dto.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import tw.com.ispan.util.BigDecimalDeserializer;

/*  ProductRequest 的責任
    1. 包含商品的整體數據（包括基本信息、價格、庫存等）。
    2. 包含一組 ProductImageRequest，負責描述該商品的所有圖片。
*/
public class ProductRequest {
    private String adminId;

    private String productName;

    private String description;

    // 前端為單選；不能為空值
    private Integer categoryId; // 用於接收 ID
    private String categoryName; // 用於查詢時顯示名稱
    private String categoryDescription;

    // 前端為多選；可為0~N個標籤
    /*
     * Q. 如果前端做成選單，還需要判斷空值嗎?
     * A. 前端做成選單，可以設定必填，但是後端也要做判斷，因為前端可以被繞過判斷(ex. Chrome devtool, postman,
     * curl)，同時為了避免惡意腳本攻擊，所以後端也要做判斷。
     */
    private Set<ProductTagRequest> tags;

    private String tagDescription;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal originalPrice;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal salePrice;

    private Integer stockQuantity;

    private String unit;

    // 自動生成：上架中、已售完 (寫入ProductService)
    // 手動調整：隱藏 (未完成)--應該寫入 adminService
    private String status;

    private LocalDate expire;

    // 前端為多選: 1~5張圖片；不能為空值
    // ✅ 確保不在這裡宣告 `List<MultipartFile>`，因為 MultipartFile 應該由 @RequestPart 接收
    // private List<MultipartFile> productImages;
    private List<String> productImages; // ✅ 只存圖片名稱，不存 MultipartFile

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<ProductTagRequest> getTags() {
        if (tags == null) {
            tags = new HashSet<>(); // 初始化為可修改的空集合
        }
        return tags;
    }

    public void setTags(Set<ProductTagRequest> tags) {
        this.tags = tags;
    }

    // public List<MultipartFile> getProductImages() {
    // return productImages;
    // }

    // public void setProductImages(List<MultipartFile> productImages) {
    // this.productImages = productImages;
    // }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<String> productImages) {
        this.productImages = productImages;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
