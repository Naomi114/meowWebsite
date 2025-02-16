package tw.com.ispan.dto.shop;

import java.util.List;

// 輸出DTO: 返回給前端的數據，經過計算或格式化
public class ProductResponse {
    private Boolean success;
    private String message;
    private ProductDTO product; // ✅ 單筆商品時使用
    private List<ProductDTO> products; // ✅ 多筆商品時使用

    public ProductResponse() {
    }

    // 無 product 的建構子: 適用 product 為 null 的狀況
    public ProductResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.product = null;
    }

    // 單筆商品建構子
    public ProductResponse(Boolean success, String message, ProductDTO product) {
        this.success = success;
        this.message = message;
        this.product = product;
        this.products = null; // 確保不會有兩個屬性同時存在
    }

    // 多筆商品建構子
    public ProductResponse(Boolean success, String message, List<ProductDTO> products) {
        this.success = success;
        this.message = message;
        this.products = products;
        this.product = null; // 確保不會有兩個屬性同時存在
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

}