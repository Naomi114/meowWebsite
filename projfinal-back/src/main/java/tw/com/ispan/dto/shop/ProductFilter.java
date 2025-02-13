package tw.com.ispan.dto.shop;

import java.math.BigDecimal;
import java.util.List;

public class ProductFilter {
     private String query;
    private Integer categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Integer> tagIds;  // ✅ 標籤 ID 陣列，方便前端高效查詢

    // ✅ 默認建構函數（Spring 需要）
    public ProductFilter() {}

    // ✅ 帶參數建構函數
    public ProductFilter(String query, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, List<Integer> tagIds) {
        this.query = query;
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.tagIds = tagIds;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    
}