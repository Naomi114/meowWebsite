package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.admin.Member;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find cart items by memberId
    List<Cart> findByMember_memberId(Integer memberId);

    // Find cart item by member and productId
    Cart findByMemberAndProductId(Member member, Integer productId);

    // Delete cart items by cartId
    void deleteByCartIdIn(List<Long> cartIds);
}
