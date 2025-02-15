package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.shop.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>, JpaSpecificationExecutor<OrderItem> {

    // 查詢已下單但未出貨的商品條目
    List<OrderItem> findByProduct_ProductIdAndOrder_OrderStatus(Integer productId, String orderStatus);

    List<OrderItem> findByOrder_OrderId(Integer orderId);

    List<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Integer orderId, Integer productId);

    // 刪除商品時，確認是否有會員下單 (by Naomi)
    @Query("SELECT COUNT(o) > 0 FROM OrderItem o WHERE o.product.id = :productId")
    boolean existsByProductId(@Param("productId") Integer productId);

}
