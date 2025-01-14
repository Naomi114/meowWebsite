package tw.com.ispan.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

// 輸入DTO: 接收前端傳來的數據
// 待檢視: 解決 ProductBean 和 CategoryBean 雙向關係的序列化問題
public class ProductRequest {
    @NotBlank
    private String productName;
    private Integer categoryId;
    @Positive
    private Integer price;
    @PositiveOrZero
    private Integer stock;
    private String description;
    private String image;
    private Boolean isOnSale;
    private Integer discount;
    private Integer discountPrice;

    @Override
    public String toString() {
        return "ProductRequest [productName=" + productName + ", categoryId=" + categoryId + ", price=" + price
                + ", stock=" + stock + ", description=" + description + ", image=" + image + ", isOnSale=" + isOnSale
                + ", discount=" + discount + ", discountPrice=" + discountPrice + "]";
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

}
