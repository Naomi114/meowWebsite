package tw.com.ispan.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import tw.com.ispan.domain.shop.ProductBean;

// 在 controller 中使用 Specification 類下的方法，組合查詢條件
public class ProductSpecifications {

    // 商品名稱模糊查詢
    public static Specification<ProductBean> hasProductName(String productName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + productName + "%");
    }

    // 價格區間查詢
    public static Specification<ProductBean> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("salePrice"), minPrice, maxPrice);
    }

    // 庫存範圍查詢
    public static Specification<ProductBean> stockBetween(Integer minStock, Integer maxStock) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("stockQuantity"), minStock, maxStock);
    }
}
