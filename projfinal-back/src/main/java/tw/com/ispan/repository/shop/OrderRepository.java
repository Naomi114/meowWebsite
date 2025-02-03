package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    // 使用 merchantTradeNo 查找订单
    Orders findByMerchantTradeNo(String merchantTradeNo);

    // 使用 transactionId 查找订单
    Orders findByTransactionId(String transactionId);

    // 新增根据 Long 类型的 orderId 查找订单的方法
    Orders findByOrderId(Long orderId);  // 注意这里传入 Long 类型的参数
}
