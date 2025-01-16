package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import tw.com.ispan.domain.shop.ProductImage;
import tw.com.ispan.domain.shop.ProductTag;

// 輸入DTO: 接收前端傳來的數據
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
    HashSet<ProductTag> tags;

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
    LinkedHashSet<ProductImage> productImages;

    // 無參建構子: 默認初始化可以留空
    public ProductRequest() {

    }

    public ProductRequest(@NotBlank String productName, String description, @NotBlank String categoryName,
            HashSet<ProductTag> tags, @NotBlank @Positive @DecimalMax("99999999.99") BigDecimal originalPrice,
            @NotBlank @Positive @DecimalMax("99999999.99") BigDecimal salePrice,
            @NotBlank @PositiveOrZero Integer stockQuantity, @NotBlank String unit, String status,
            @NotBlank @Future LocalDate expire, @NotBlank LinkedHashSet<ProductImage> productImages) {
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

    public void setTags(HashSet<ProductTag> tags) {
        this.tags = tags;
    }

    public LinkedHashSet<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(LinkedHashSet<ProductImage> productImages) {
        this.productImages = productImages;
    }

}
