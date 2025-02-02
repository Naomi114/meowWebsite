package tw.com.ispan.dto;

import java.util.List;

import tw.com.ispan.domain.shop.Product;

// 輸出DTO: 返回給前端的數據，經過計算或格式化
public class ProductResponse {
    private Boolean success;
    private String message;
    private Product product; // 單商品數據
    private List<Product> products; // 多商品列表
    private Long count;
    
    public ProductResponse() {
    }

    public ProductResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ProductResponse(Boolean success, String message, Product product, List<Product> products, Long count) {
        this.success = success;
        this.message = message;
        this.product = product;
        this.products = products;
        this.count = count;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
