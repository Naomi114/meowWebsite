package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    boolean existsByProductName(String productName);

    // 查詢某類別下的所有商品
    List<Product> findByCategory(Category category);

    // 查詢多個類別下的所有商品
    List<Product> findByCategoryIn(List<Category> categories);

    // 分頁
    Page<Product> findAll(Pageable pageable);

    // 因為 Product實體 設置 FetchType=LAZY 的設置會導致 Category 沒有載入，引發 Null 錯誤
    // 解決方法: 在查詢 Product 時才載入 Category
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.productId = :id")
    Product findByIdWithCategory(@Param("id") Integer id);

    // 多條件查詢
    // MSSQL 無法解析 `IN` 、LIKE 等關鍵字
    // @Query("SELECT p FROM Product p WHERE " +
    // "(:query IS NULL OR p.productName LIKE %:query%) " +
    // "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
    // "AND (:minPrice IS NULL OR p.salePrice >= :minPrice) " +
    // "AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice) " +
    // "AND (:tags IS NULL OR p.tags IN :tags)")
    // List<Product> findProductsByFilter(@Param("query") String query,
    // @Param("categoryId") Integer categoryId,
    // @Param("minPrice") BigDecimal minPrice,
    // @Param("maxPrice") BigDecimal maxPrice,
    // @Param("tags") List<String> tags);
}
