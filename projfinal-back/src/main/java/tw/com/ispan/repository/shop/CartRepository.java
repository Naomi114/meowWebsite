package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.CartBean;

public class CartRepository extends JpaRepository<CartBean, Integer> {

}
