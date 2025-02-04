package tw.com.ispan.dto;

import java.util.List;

import tw.com.ispan.domain.shop.Category;

public class CategoryResponse {
    private Boolean success;
    private String message;
    private Integer categoryId;
    private String categoryName;
    private String defaultUnit;
    private List<ProductDTO> products;
    private List<Category> categories;

    public CategoryResponse() {
    }

    public CategoryResponse(Boolean success, String message, Integer categoryId, String categoryName,
            String defaultUnit, List<ProductDTO> products, List<Category> categories) {
        this.success = success;
        this.message = message;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.defaultUnit = defaultUnit;
        this.products = products;
        this.categories = categories;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
