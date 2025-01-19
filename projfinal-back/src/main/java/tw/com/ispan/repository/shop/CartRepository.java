package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.admin.Member;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 根據 memberId 查詢購物車資料
    List<Cart> findByMember_memberId(Integer memberId);  // 使用 Integer 來匹配 memberId

    // 根據 Member 和 productId 查詢購物車資料，這裡返回的是 Cart 而非 List<Cart>
    Cart findByMemberAndProductId(Member member, Integer productId); // 修正為 Integer 型別，保持一致

    // 根據多個 cartId 刪除購物車資料
    void deleteByCartIdIn(List<Long> cartIds); // 使用 Long 型別作為 cartId
}
