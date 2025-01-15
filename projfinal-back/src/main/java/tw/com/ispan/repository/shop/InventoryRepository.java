package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // 查詢商品庫存 (20250114 by Naomi)
    List<Inventory> findByProductId(Integer productId);

}