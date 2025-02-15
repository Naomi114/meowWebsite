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
    private List<ProductDTO> products;

    public CategoryDTO() {
    }

    // ✅ 建構子: 接收 Category 和 ProductDTO 陣列
    public CategoryDTO(Category category, List<ProductDTO> productDTOs) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categoryDescription = category.getCategoryDescription();
        this.defaultUnit = category.getDefaultUnit();
        this.products = productDTOs;
    }

    // ✅ 建構子: 只接收 Category (無商品列表)
    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categoryDescription = category.getCategoryDescription();
        this.defaultUnit = category.getDefaultUnit();
        this.products = category.getProducts().stream()
                .map(ProductDTO::new) // 確保商品被轉為 ProductDTO
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

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

}
