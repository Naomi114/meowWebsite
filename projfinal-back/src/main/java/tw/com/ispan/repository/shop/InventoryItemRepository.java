package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    // 查詢商品庫存 (20250114 by Naomi)
    List<InventoryItem> findByProduct_ProductId(Integer productId);

}
