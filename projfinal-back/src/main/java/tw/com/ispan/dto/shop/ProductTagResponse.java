package tw.com.ispan.dto.shop;

import java.util.List;

import tw.com.ispan.domain.shop.ProductTag;

public class ProductTagResponse {
    private Boolean success;
    private String message;
    private List<ProductDTO> products;
    private List<ProductTag> tags; // 標籤列表
    private Long count;

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

    public List<ProductTag> getTags() {
        return tags;
    }

    public void setTags(List<ProductTag> tags) {
        this.tags = tags;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

}
