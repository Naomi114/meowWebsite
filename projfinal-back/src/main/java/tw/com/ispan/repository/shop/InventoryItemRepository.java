package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.InventoryItem;
import tw.com.ispan.domain.shop.Product;

public interface InventoryItemRepository
        extends JpaRepository<InventoryItem, Integer>, JpaSpecificationExecutor<InventoryItem> {

    // 查詢商品庫存
    List<InventoryItem> findByProduct_ProductId(Integer productId);

    void deleteByProduct(Product product);

}
