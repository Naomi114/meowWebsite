package tw.com.ispan.domain.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "InventoryItem")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryItemId;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer actualStock;

    // 商品刪除時，保留相關的庫存異動記錄
    // 將多對一關係設置為可選（optional = true），允許商品刪除後，該關聯字段為 null
    @ManyToOne(optional = true)
    @JoinColumn(name = "FK_productId", foreignKey = @ForeignKey(name = "fkc_product_id"), nullable = true)
    private Product product;

    // 盤點任務刪除時，保留相關的庫存異動記錄
    // 將多對一關係設置為可選（optional = true），允許盤點任務刪除後，該關聯字段為 null
    @ManyToOne(optional = true)
    @JoinColumn(name = "FK_inventoryId", foreignKey = @ForeignKey(name = "fkc_inventory_id"), nullable = true)
    private Inventory inventory;

    public InventoryItem() {
    }

    public InventoryItem(Integer inventoryItemId, Integer stockQuantity, Integer actualStock, Product product,
            Inventory inventory) {
        this.inventoryItemId = inventoryItemId;
        this.stockQuantity = stockQuantity;
        this.actualStock = actualStock;
        this.product = product;
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "InventoryItem [inventoryItemId=" + inventoryItemId + ", stockQuantity=" + stockQuantity
                + ", actualStock=" + actualStock
                + ", product=" + product + ", inventory=" + inventory + "]";
    }

    public Integer getAuditId() {
        return inventoryItemId;
    }

    public void setAuditId(Integer inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getActualStock() {
        return actualStock;
    }

    public void setActualStock(Integer actualStock) {
        this.actualStock = actualStock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

}
