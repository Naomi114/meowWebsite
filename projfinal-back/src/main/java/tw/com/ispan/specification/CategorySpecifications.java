package tw.com.ispan.specification;

import org.springframework.data.jpa.domain.Specification;

import tw.com.ispan.domain.shop.Category;

public class CategorySpecifications {

    // 類別模糊查詢
    public static Specification<Category> hasCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("categoryName"),
                "%" + categoryName + "%");
    }

}
