package tw.com.ispan.repository.shop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

    // 使用 merchantTradeNo 查找訂單
    Orders findByMerchantTradeNo(String merchantTradeNo);

    // 使用 transactionId 查找訂單
    Orders findByTransactionId(String transactionId);

    // 根據 Long 類型的 orderId 查找訂單的方法
    Orders findByOrderId(Long orderId); // 注意這裡傳入 Long 類型的參數

    // 可選：若您希望查找訂單時避免 NullPointerException，這裡也可以返回 Optional<Orders>
    Optional<Orders> findOptionalByMerchantTradeNo(String merchantTradeNo);
}
