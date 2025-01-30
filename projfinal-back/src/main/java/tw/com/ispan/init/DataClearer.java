package tw.com.ispan.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.repository.shop.OrderItemRepository;
import tw.com.ispan.repository.shop.OrderRepository;
import tw.com.ispan.domain.shop.OrderItem;

@Component
public class DataClearer {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void clearOrderData() {
        // 確保清除資料庫中的所有資料，這裡不依賴於關聯
        orderItemRepository.deleteAll();  // 刪除所有 OrderItem
        orderRepository.deleteAll();  // 刪除所有 Order

        // 輸出清除結果
        System.out.println("Orders and OrderItems tables cleared.");
    }
}
