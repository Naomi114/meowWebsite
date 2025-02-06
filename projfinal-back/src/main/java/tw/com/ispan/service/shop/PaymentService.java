package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.repository.shop.OrderRepository;
import tw.com.ispan.dto.PaymentRequest;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    // 处理支付结果
    public void handlePaymentResult(String transactionId, String merchantTradeNo, boolean success) {
        // 根据 MerchantTradeNo 查询订单
        Orders order = orderRepository.findByMerchantTradeNo(merchantTradeNo);

        if (order != null) {
            // 根据支付结果更新订单状态
            if (success) {
                // 更新 merchantTradeNo 和 transactionId
                order.setMerchantTradeNo(merchantTradeNo); // 通过绿界返回的 merchantTradeNo 更新
                order.setTransactionId(transactionId); // 通过绿界返回的 transactionId 更新
                order.setOrderStatus("已支付"); // 设置订单状态为已支付
            } else {
                order.setOrderStatus("支付失败"); // 设置订单状态为支付失败
            }
            orderRepository.save(order); // 保存更新后的订单
        }
    }

    // 新的支付处理方法，使用 PaymentRequest 作为参数
    public String processPayment(PaymentRequest paymentRequest) {
        // 假设这里调用了绿界支付接口并返回支付处理结果
        if ("success".equals(paymentRequest.getPaymentStatus())) {
            return "Payment Processed successfully for amount: " + paymentRequest.getAmount();
        } else {
            return "Payment Failed for amount: " + paymentRequest.getAmount();
        }
    }
}
