package tw.com.ispan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

// 輸入DTO: 接收前端傳來的數據
public class ProductRequest {
    @NotBlank
    private String productName;
    private Integer categoryId;
    @Positive
    private BigDecimal price;
    @PositiveOrZero
    private Integer stockQuantity;
    private String description;
    private LocalDate expire;
    private String unit;

    @Override
    public String toString() {
        return "ProductRequest [productName=" + productName + ", categoryId=" + categoryId + ", price=" + price
                + ", stockQuantity=" + stockQuantity + ", description=" + description + ", expire=" + expire + ", unit="
                + unit + "]";
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
