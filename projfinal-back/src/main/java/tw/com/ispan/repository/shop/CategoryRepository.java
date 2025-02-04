package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.CartItem;

public interface CategoryRepository
        extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    // 模糊查詢
    List<Category> findByCategoryNameContaining(String keyword);

    // 精確查詢
    Optional<Category> findByCategoryName(String categoryName);

}
