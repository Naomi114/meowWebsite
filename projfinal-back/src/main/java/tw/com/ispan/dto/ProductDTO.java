package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductImage;

// 用於封裝 Product 的資料，確保 imageUrls 存在
public class ProductDTO {
    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private Integer stockQuantity;
    private String unit;
    private String status;
    private LocalDate expire;
    private List<String> imageUrls; // ✅ 用於 API 回應圖片 URL

    // ✅ 透過 Product Entity 建立 DTO
    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.originalPrice = product.getOriginalPrice();
        this.salePrice = product.getSalePrice();
        this.stockQuantity = product.getStockQuantity();
        this.unit = product.getUnit();
        this.status = product.getStatus();
        this.expire = product.getExpire();

        // ✅ 取得最多 5 張圖片 URL
        this.imageUrls = extractImageUrls(product);
    }

    // ✅ 取得 `imageUrls`
    private List<String> extractImageUrls(Product product) {
        if (product.getProductImages() != null) {
            return product.getProductImages().stream()
                    .limit(5) // 限制最多 5 張
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toList());
        }
        return List.of(); // ✅ 若無圖片則回傳空列表
    }

    public List<String> getImageUrls() {
        return imageUrls;
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

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

}
