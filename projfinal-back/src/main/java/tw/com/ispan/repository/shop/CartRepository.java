package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    // 查找單個會員對應的購物車
    Cart findByMember_Id(Integer memberId); // 返回單個 Cart，而非 List<Cart>
}
