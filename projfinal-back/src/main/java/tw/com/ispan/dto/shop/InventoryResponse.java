package tw.com.ispan.dto.shop;

import java.util.List;

import tw.com.ispan.domain.shop.InventoryItem;

public class InventoryResponse {
    private Boolean success;
    private String message;
    private List<InventoryItem> inventoryItems; // 盤點項目列表
    private Long count;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
