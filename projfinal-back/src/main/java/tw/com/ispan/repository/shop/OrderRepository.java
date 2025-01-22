package tw.com.ispan.repository.shop;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    // 根据订单 ID 查找订单
    Orders findById(int orderId);
}
