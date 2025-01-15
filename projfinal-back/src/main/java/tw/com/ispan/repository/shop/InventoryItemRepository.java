package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.InventoryItem;

public interface InventoryItemRepository
        extends JpaRepository<InventoryItem, Integer>, JpaSpecificationExecutor<InventoryItem> {

    // 查詢商品庫存 (20250114 by Naomi)
    List<InventoryItem> findByProduct_ProductId(Integer productId);

}
