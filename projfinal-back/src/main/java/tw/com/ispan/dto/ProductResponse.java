package tw.com.ispan.dto;

import java.util.List;

import tw.com.ispan.domain.shop.ProductBean;

// 輸出DTO: 返回給前端的數據，經過計算或格式化
public class ProductResponse {
    private Boolean success;
    private String message;
    private ProductBean product; // 單商品數據
    private List<ProductBean> products; // 多商品列表
    private Long count;

    // 無參建構子: 默認初始化可以留空
    public ProductResponse() {
    }

    public ProductResponse(ProductBean productBean) {

    }

    @Override
    public String toString() {
        return "ProductResponse [success=" + success + ", message=" + message + ", product=" + product + ", products="
                + products + ", count=" + count + "]";
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

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
