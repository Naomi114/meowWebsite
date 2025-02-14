package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductImage;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Integer>, JpaSpecificationExecutor<ProductImage> {

    void deleteByProduct(Product product);

    // 刪除商品時，刪除關聯圖片；對應實體的資料表名稱
    // @OneToMany 刪除商品時，解除@Table表格關聯
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ProductImage WHERE FK_productId = :productId", nativeQuery = true)
    void deleteImagesByProductId(@Param("productId") Integer productId);

}
