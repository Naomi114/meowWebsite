package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    // 商品搜尋頁>>類別模糊查詢
    // 包含關鍵字
    List<Category> findByCategoryNameContaining(String keyword);

    // 以關鍵字開頭
    List<Category> findByCategoryNameStartingWith(String prefix);

    // 以關鍵字結尾
    List<Category> findByCategoryNameEndingWith(String suffix);

}