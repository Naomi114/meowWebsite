package tw.com.ispan.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Inventory;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.repository.shop.InventoryRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public void updateInventory(Integer productId, Integer newQuantity) {

        // 盤點時，更新數量影響到已經下單、未出貨的商品數量
        List<Inventory> inventories = inventoryRepository.findByProductId(productId);
        if (inventories.isEmpty()) {
            throw new RuntimeException("Inventory not found");
        }
        Inventory inventory = inventories.get(0);
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);

        // 檢查影響的訂單項目
        List<OrderItem> affectedItems = orderItemRepository.findByProductIdAndOrderStatus(productId, "PENDING");
        for (OrderItem item : affectedItems) {
            if (item.getOrderQuantity() > newQuantity) {
                // 通知或標記訂單條目，庫存不足
                item.setStatus("庫存不足");
                // 可選擇直接更新數量
                item.setOrderQuantity(newQuantity);
            }
        }
        orderItemRepository.saveAll(affectedItems);
    }

}
