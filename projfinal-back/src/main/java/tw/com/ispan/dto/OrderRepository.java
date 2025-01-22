package tw.com.ispan.dto;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    // Modify the method signature to use Long if needed
    Orders findById(int orderId);
}
