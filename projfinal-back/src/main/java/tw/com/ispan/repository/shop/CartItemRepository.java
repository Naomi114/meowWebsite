package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.com.ispan.domain.shop.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // 根據 cartId 查詢 CartItem
    List<CartItem> findByCart_CartId(int cartId);

    // 依照會員和商品ID找出某商品的項目
    CartItem findByCart_Member_IdAndProduct_ProductId(Integer memberId, Integer productId);

    // 依照會員ID找出所有 CartItem
    List<CartItem> findByCart_Member_Id(Integer memberId);

    // 查詢購物車商品（最多1000個）
    List<CartItem> findTop1000ByCart_Member_Id(Integer memberId);

    // 根據 cartId 刪除商品
    void deleteByCart_CartIdIn(List<Integer> cartIds);

    // 根據訂單 ID 尋找購物車商品
    List<CartItem> findByOrder_OrderId(Integer orderId);

    // 刪除指定的 CartItem
    void deleteAllByCartItemIdIn(List<Integer> cartItemIds);
}
