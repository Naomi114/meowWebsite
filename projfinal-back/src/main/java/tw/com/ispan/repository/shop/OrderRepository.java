package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.aspectj.weaver.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

    // 使用 merchantTradeNo 查找訂單
    Orders findByMerchantTradeNo(String merchantTradeNo);

    // 使用 transactionId 查找訂單
    Orders findByTransactionId(String transactionId);

    List<Orders> findByMember_MemberId(int memberId);

}
