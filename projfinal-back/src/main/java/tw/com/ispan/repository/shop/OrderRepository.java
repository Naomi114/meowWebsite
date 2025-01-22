package tw.com.ispan.repository.shop;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import tw.com.ispan.domain.shop.Orders;

public interface OrderRepository extends CrudRepository<Orders, Integer> {
    // Modify the method signature to use Long if needed
    Orders findById(int orderId);
}
