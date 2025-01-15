package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // 查詢已下單但未出貨的商品條目 (20250114 by Naomi)
    List<OrderItem> findByProduct_ProductIdAndOrder_OrderStatus(Integer productId, String orderStatus);

}
