package tw.com.ispan.repository.shop;

import org.springframework.data.repository.CrudRepository;
import tw.com.ispan.domain.shop.Orders;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Orders, Integer> {
    @SuppressWarnings("null")
    Optional<Orders> findById(Integer orderId);
}
