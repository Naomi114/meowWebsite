package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // 查詢所有未下單且包含指定商品的購物車項目；調整盤點數量時在CartService中使用 (by Naomi)
    List<CartItem> findByProduct_ProductIdAndOrderIsNull(Integer productId);

}
