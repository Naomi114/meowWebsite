package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.InventoryItem;

public interface StockAuditRepository extends JpaRepository<InventoryItem, Integer> {

}
