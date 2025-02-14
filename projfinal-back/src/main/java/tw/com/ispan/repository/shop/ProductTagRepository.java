package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.ProductTag;

public interface ProductTagRepository extends JpaRepository<ProductTag, Integer>, JpaSpecificationExecutor<ProductTag> {

    // 模糊查詢
    List<ProductTag> findByTagNameContaining(String keyword);

    // 精確查詢
    Optional<ProductTag> findByTagName(String tagName);

    // @ManyToMany刪除商品時，解除product_tag表格關聯
    // 取 @JoinTable name的表格名稱
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_tag WHERE product_id = :productId", nativeQuery = true)
    void deleteTagsByProductId(@Param("productId") Integer productId);

}
