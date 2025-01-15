package tw.com.ispan.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.InventoryItem;
import tw.com.ispan.domain.shop.OrderItem;
import tw.com.ispan.repository.shop.InventoryItemRepository;
import tw.com.ispan.repository.shop.InventoryRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public void updateInventory(Integer productId, Integer newQuantity) {

        // 盤點時，更新數量影響到已經下單、未出貨的商品數量
        List<InventoryItem> inventoryItems = inventoryItemRepository.findByProduct_ProductId(productId);
        if (inventoryItems.isEmpty()) {
            throw new RuntimeException("Inventory not found for product ID: " + productId);
        }
        for (InventoryItem item : inventoryItems) {
            item.setStockQuantity(newQuantity);
        }
        inventoryItemRepository.saveAll(inventoryItems);

        // 檢查影響的訂單項目
        List<OrderItem> affectedItems = orderItemRepository.findByProduct_ProductIdAndOrder_OrderStatus(productId,
                "庫存不足，請跟客服聯繫");
        for (OrderItem item : affectedItems) {
            if (item.getOrderQuantity() > newQuantity) {
                // 修改訂單狀態
                item.setStatus("庫存不足，請跟客服聯繫");
            }
        }
        orderItemRepository.saveAll(affectedItems);
    }

}
