package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
