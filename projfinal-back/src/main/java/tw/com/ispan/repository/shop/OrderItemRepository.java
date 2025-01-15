package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>, JpaSpecificationExecutor<OrderItem> {

    // 查詢已下單但未出貨的商品條目 --是否可以用JpaSpecificationExecutor改寫???? (20250115 by Naomi)
    List<OrderItem> findByProduct_ProductIdAndOrder_OrderStatus(Integer productId, String orderStatus);

}
