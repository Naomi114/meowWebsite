package tw.com.ispan.specification;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;

// 在 controller 中使用 Specification 類下的方法，組合查詢條件
public class ProductSpecifications {

    // 商品名稱模糊查詢
    public static Specification<Product> hasProductName(String productName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + productName + "%");
    }

    // 價格區間查詢
    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("salePrice"), minPrice, maxPrice);
    }

    // 庫存範圍查詢
    public static Specification<Product> stockBetween(Integer minStock, Integer maxStock) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("stockQuantity"), minStock, maxStock);
    }

    // 新增按類別查詢商品
    public static Specification<Product> hasCategory(Category category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Product> hasCategoryIn(List<Category> categories) {
        return (root, query, criteriaBuilder) -> root.get("category").in(categories);
    }

}
