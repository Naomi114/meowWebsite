package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.InventoryBean;

public interface InventoryRepository extends JpaRepository<InventoryBean, Integer> {

}