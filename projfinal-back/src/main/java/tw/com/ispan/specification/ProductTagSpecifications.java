package tw.com.ispan.specification;

import org.springframework.data.jpa.domain.Specification;

import tw.com.ispan.domain.shop.ProductTag;

public class ProductTagSpecifications {

    // 標籤模糊查詢
    public static Specification<ProductTag> hasTagName(String tagName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("tagName"), "%" + tagName + "%");
    }

}