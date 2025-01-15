package tw.com.ispan.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import tw.com.ispan.domain.shop.ProductBean;

public class ProductSpecifications {

    // 商品名稱模糊查詢: 商品名稱關鍵字+價格區間
    public static Specification<ProductBean> hasProductName(String productName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + productName + "%");
    }

    public static Specification<ProductBean> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("salePrice"), minPrice, maxPrice);
    }
}
