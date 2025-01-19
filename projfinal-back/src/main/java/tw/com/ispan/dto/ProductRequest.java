package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import tw.com.ispan.domain.shop.ProductImage;
import tw.com.ispan.domain.shop.ProductTag;

/*  ProductRequest 的責任
    1. 包含商品的整體數據（包括基本信息、價格、庫存等）。
    2. 包含一組 ProductImageRequest，負責描述該商品的所有圖片。
*/
public class ProductRequest {
    // @NotBlank 只能限制String類型，其他類不為null需要在service層判斷
    @NotBlank
    String productName;

    String description;

    // 前端為單選；不能為空值
    @NotBlank
    String categoryName;

    // 前端為多選；不能為空值
    /*
     * Q. 如果前端做成選單，還需要判斷空值嗎?
     * A. 前端做成選單，可以設定必填，但是後端也要做判斷，因為前端可以被繞過判斷(ex. Chrome devtool, postman,
     * curl)，同時為了避免惡意腳本攻擊，所以後端也要做判斷。
     */
    Set<ProductTag> tags;

    // 前端為填入框；不能為空值
    @Positive
    @DecimalMax(value = "99999999.99")
    BigDecimal originalPrice;

    // 前端為填入框；不能為空值
    @Positive
    @DecimalMax(value = "99999999.99")
    BigDecimal salePrice;

    // 前端為填入框；不能為空值
    @PositiveOrZero
    Integer stockQuantity;

    @NotBlank
    String unit;

    // 自動生成：上架中、已售完 (20250116 寫入ProductService)
    // 手動調整：隱藏 (未完成)--應該寫入 adminService
    @NotBlank
    String status;

    @Future
    LocalDate expire;

    // 前端為多選: 1~5張圖片；不能為空值
    List<ProductImage> productImages;

    // 無參建構子: 默認初始化可以留空
    public ProductRequest() {

    }

    public ProductRequest(@NotBlank String productName, String description, @NotBlank String categoryName,
            Set<ProductTag> tags, @NotBlank @Positive @DecimalMax("99999999.99") BigDecimal originalPrice,
            @NotBlank @Positive @DecimalMax("99999999.99") BigDecimal salePrice,
            @NotBlank @PositiveOrZero Integer stockQuantity, @NotBlank String unit, String status,
            @NotBlank @Future LocalDate expire, @NotBlank List<ProductImage> productImages) {
        this.productName = productName;
        this.description = description;
        this.categoryName = categoryName;
        this.tags = tags;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.status = status;
        this.expire = expire;
        this.productImages = productImages;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<ProductTag> getTags() {
        return tags;
    }

    public void setTags(Set<ProductTag> tags) {
        this.tags = tags;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

}
