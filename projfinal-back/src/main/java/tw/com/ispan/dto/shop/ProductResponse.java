package tw.com.ispan.dto.shop;

import java.util.List;
import java.util.stream.Collectors;

import tw.com.ispan.domain.shop.Product;

// 輸出DTO: 返回給前端的數據，經過計算或格式化
public class ProductResponse {
    private Boolean success;
    private String message;
    private ProductDTO product;
    private List<ProductDTO> products;
    private Long count;

    public ProductResponse() {
    }

    public ProductResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ProductResponse(Boolean success, String message, List<Product> products, Long count) {
        this.success = success;
        this.message = message;
        this.count = count;

        // ✅ 轉換 `Product` 為 `ProductDTO`
        this.products = products.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}