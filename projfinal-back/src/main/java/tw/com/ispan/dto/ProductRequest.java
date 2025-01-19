package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/*  ProductRequest 的責任
    1. 包含商品的整體數據（包括基本信息、價格、庫存等）。
    2. 包含一組 ProductImageRequest，負責描述該商品的所有圖片。
*/
public class ProductRequest {
    // @NotBlank 只能限制String類型，其他類不為null需要在service層判斷
    @NotBlank(message = "商品名稱不能為空")
    private String productName;

    private String description;

    // 前端為單選；不能為空值
    @NotBlank(message = "商品類別不能為空")
    private String categoryName;

    // 前端為多選；可為0~N個標籤
    /*
     * Q. 如果前端做成選單，還需要判斷空值嗎?
     * A. 前端做成選單，可以設定必填，但是後端也要做判斷，因為前端可以被繞過判斷(ex. Chrome devtool, postman,
     * curl)，同時為了避免惡意腳本攻擊，所以後端也要做判斷。
     */
    private Set<ProductTagRequest> tags;

    // 前端為填入框；不能為空值
    @Positive(message = "價格必須為正數")
    @NotNull(message = "商品原價不能為空")
    @DecimalMax(value = "99999999.99")
    private BigDecimal originalPrice;

    // 前端為填入框；不能為空值
    @Positive(message = "價格必須為正數")
    @NotNull(message = "商品售價不能為空")
    @DecimalMax(value = "99999999.99")
    private BigDecimal salePrice;

    // 前端為填入框；不能為空值
    @PositiveOrZero(message = "數量必須為正整數")
    @NotNull(message = "商品數量不能為空")
    private Integer stockQuantity;

    @NotBlank
    private String unit;

    // 自動生成：上架中、已售完 (20250116 寫入ProductService)
    // 手動調整：隱藏 (未完成)--應該寫入 adminService
    @NotBlank
    private String status;

    @Future
    private LocalDate expire;

    // 前端為多選: 1~5張圖片；不能為空值
    @NotNull(message = "商品圖片不能為空")
    private List<ProductImageRequest> productImages;

    // 無參建構子: 默認初始化可以留空
    public ProductRequest() {

    }

    public ProductRequest(@NotBlank String productName, String description, @NotBlank String categoryName,
            Set<ProductTagRequest> tags, @NotBlank @Positive @DecimalMax("99999999.99") BigDecimal originalPrice,
            @NotBlank @Positive @DecimalMax("99999999.99") BigDecimal salePrice,
            @NotBlank @PositiveOrZero Integer stockQuantity, @NotBlank String unit, String status,
            @NotBlank @Future LocalDate expire, @NotBlank List<ProductImageRequest> productImages) {
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

    public void setProductImages(List<ProductImageRequest> productImages) {
        this.productImages = productImages;
    }

    public List<ProductImageRequest> getProductImages() {
        return productImages;
    }

   public Set<ProductTagRequest> getTags() {
        return tags;    
    }

    public void setTags(Set<ProductTagRequest> tags) {
        this.tags = tags;
    }

}
