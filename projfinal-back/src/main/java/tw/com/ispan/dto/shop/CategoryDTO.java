package tw.com.ispan.dto.shop;

import java.util.List;
import java.util.stream.Collectors;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;

public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
    private String defaultUnit;
    private List<Integer> productIds; // 只存 `productId`，避免循環關聯

    public CategoryDTO() {
    }

    // ✅ 建構子: 只接收 Category (轉換 productId)
    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categoryDescription = category.getCategoryDescription();
        this.defaultUnit = category.getDefaultUnit();
        this.productIds = category.getProducts().stream()
                .map(Product::getProductId) // ✅ 只存 productId
                .collect(Collectors.toList());
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

}
