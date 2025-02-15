package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.InventoryItem;
import tw.com.ispan.domain.shop.Product;

public interface InventoryItemRepository
        extends JpaRepository<InventoryItem, Integer>, JpaSpecificationExecutor<InventoryItem> {

    // 查詢商品庫存
    List<InventoryItem> findByProduct_ProductId(Integer productId);

    void deleteByProduct(Product product);

    // 刪除商品時，解除關聯；對應實體的資料表名稱
    // @OneToMany 刪除商品時，解除@Table表格關聯
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM InventoryItem WHERE FK_productId = :productId", nativeQuery = true)
    void removeInventoryItemByProductId(@Param("productId") Integer productId);

}
