package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    // 商品搜尋頁>>類別模糊查詢 (還沒用到)
    List<Category> findByCategoryNameContaining(String keyword);

    // 精確查詢
    Optional<Category> findByCategoryName(String categoryName);

}