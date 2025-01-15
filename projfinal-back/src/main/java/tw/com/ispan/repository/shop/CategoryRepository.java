package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.CategoryBean;

public interface CategoryRepository
        extends JpaRepository<CategoryBean, Integer>, JpaSpecificationExecutor<CategoryBean> {
    // 商品搜尋頁>>類別模糊查詢
    // 包含關鍵字
    List<CategoryBean> findByCategoryNameContaining(String keyword);

    // 以關鍵字開頭
    List<CategoryBean> findByCategoryNameStartingWith(String prefix);

    // 以關鍵字結尾
    List<CategoryBean> findByCategoryNameEndingWith(String suffix);

}