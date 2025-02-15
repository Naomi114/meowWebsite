package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.CartItem;

public interface CategoryRepository
        extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    // 模糊查詢
    List<Category> findByCategoryNameContaining(String keyword);

    // 精確查詢
    Optional<Category> findByCategoryName(String categoryName);

    // @ManyToOne 刪除商品時，解除 Category 與 Product 的關聯，但不刪除 Category 本身;取 @JoinTable
    // name的表格名稱
    // 根據 productId，將 FK_categoryId 設為 NULL，解除 Product 和 Category 之間的關聯。
    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET FK_categoryId = NULL WHERE productId = :productId", nativeQuery = true)
    void removeCategoryFromProducts(@Param("productId") Integer productId);
}
