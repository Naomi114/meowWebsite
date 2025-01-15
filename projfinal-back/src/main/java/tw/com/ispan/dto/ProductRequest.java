package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @NotBlank
    private String productName;

    String description;

    @Positive
    @DecimalMax(value = "99999999.99")
    BigDecimal originalPrice;

    @Positive
    @DecimalMax(value = "99999999.99")
    BigDecimal salePrice;

    @PositiveOrZero
    Integer stockQuantity;

    String unit;
    String status;

    @Future
    LocalDate expire;

    // 前端為單選
    String categoryName;

    // 前端為多選
    Set<ProductTag> tags;

    @NotBlank
    Set<ProductImage> productImages;

    // 無參建構子: 默認初始化可以留空
    public ProductRequest() {

    }

    public ProductRequest(@NotBlank String productName, String description,
            @Positive @DecimalMax("99999999.99") BigDecimal originalPrice,
            @Positive @DecimalMax("99999999.99") BigDecimal salePrice, @PositiveOrZero Integer stockQuantity,
            String unit, String status, @Future LocalDate expire, String categoryName, Set<ProductTag> tags,
            @NotBlank Set<ProductImage> productImages) {
        this.productName = productName;
        this.description = description;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.status = status;
        this.expire = expire;
        this.categoryName = categoryName;
        this.tags = tags;
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

    public Set<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(Set<ProductImage> productImages) {
        this.productImages = productImages;
    }

}
