package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.web.multipart.MultipartFile;

/*  ProductRequest 的責任
    1. 包含商品的整體數據（包括基本信息、價格、庫存等）。
    2. 包含一組 ProductImageRequest，負責描述該商品的所有圖片。
*/
public class ProductRequest {
    @NotBlank(message = "管理員帳號不能為空")
    private String adminId;

    // @NotBlank 只能限制String類型，其他類不為null需要在service層判斷
    @NotBlank(message = "商品名稱不能為空")
    private String productName;

    private String description;

    // 前端為單選；不能為空值
    @NotBlank(message = "商品類別不能為空")
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

    // 若選擇類別已存在，則進入service層後會自動帶入預設單位；所以不能使用 @NotBlank
    // 或是新建類別、單位
    private String unit;

    // 自動生成：上架中、已售完 (20250116 寫入ProductService)
    // 手動調整：隱藏 (未完成)--應該寫入 adminService
    // @NotBlank
    // private String status;

    @Future
    private LocalDate expire;

    // 前端為多選: 1~5張圖片；不能為空值
    @NotNull(message = "商品圖片不能為空")
    private List<MultipartFile> productImages;

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

    // public String getStatus() {
    //     return status;
    // }

    // public void setStatus(String status) {
    //     this.status = status;
    // }

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

    public List<MultipartFile> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<MultipartFile> productImages) {
        this.productImages = productImages;
    }

    public String getCategoryDescription() {
        return categoryDescription;
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
