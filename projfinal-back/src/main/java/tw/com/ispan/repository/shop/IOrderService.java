package tw.com.ispan.repository.shop;

import java.util.Optional;

import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.dto.shop.OrderDTO;

public interface IOrderService {

    /**
     * 通过订单ID获取订单DTO
     * 
     * @param orderId 订单ID
     * @return 订单实体
     */
    Orders getOrderDTOById(Long orderId);

    /**
     * 更新 ECPay 交易ID
     * 
     * @param orderId         订单ID
     * @param merchantTradeNo ECPay 返回的商户交易编号 (由系统生成)
     */
    void updateEcpayTransactionId(Integer orderId, String merchantTradeNo);

    void updateMerchantTradeNo(Integer orderId, String merchantTradeNo);

    Optional<Orders> getOrderByMerchantTradeNo(String merchantTradeNo);

    /**
     * 根据 ECPay 交易ID 获取订单
     * 
     * @param transactionId ECPay 交易ID (由ECPay返回)
     * @return 订单实体的Optional包装
     */
    Optional<Orders> getOrderByEcpayTransactionId(String transactionId);

    Optional<Orders> getOrderIdByEcpayTransactionId(String ecpayTransactionId);

    /**
     * 通过商户交易编号获取订单
     * 
     * @param merchantTradeNo 商户交易编号
     * @return 订单实体的Optional包装
     */
}
