package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.dto.CartItemResponse; // Make sure the import is correct

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // 查找會員購物車中的所有商品項目，並返回 CartItemResponse
    // 查找會員購物車中的所有商品項目，並返回 CartItemResponse
    @Query("SELECT new tw.com.ispan.dto.CartItemResponse(ci) FROM CartItem ci " +
            "JOIN ci.product p " + // Join with Product entity to fetch product details
            "WHERE ci.cart.member.id = :memberId") // Only filter by Member ID to get items in their cart
    List<CartItemResponse> findCartItemsForMember(Integer memberId);

    // 根據購物車項目的 ID 列表刪除商品
    void deleteAllByCartItemIdIn(List<Integer> cartItemIds);

    // 根據會員和商品 ID 查找某商品的項目
    CartItem findByCart_Member_IdAndProduct_ProductId(Integer memberId, Integer productId);

    // 根據會員 ID 查找所有 CartItem
    List<CartItem> findByCart_Member_Id(Integer memberId);
}
