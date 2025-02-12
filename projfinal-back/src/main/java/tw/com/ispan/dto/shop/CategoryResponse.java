package tw.com.ispan.dto.shop;

import java.util.List;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;

public class CategoryResponse {
    private Boolean success;
    private String message;
    private List<Product> products;
    private List<Category> categories;

    public CategoryResponse() {
    }

    public CategoryResponse(Boolean success, String message,List<Product> products, List<Category> categories) {
        this.success = success;
        this.message = message;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
