package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Admin;

@Entity
@Table(name = "Inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = true)
    private String diffReason;

    @Column(nullable = false)
    private String inventoryStatus;

    @Column(nullable = false)
    private LocalDateTime checkAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;

    // 單向一對多關聯
    // cascade = CascadeType.remove 因為庫存紀錄對商品數量有影響，所以不允許連動刪除庫存異動記錄，只能手動刪除
    @OneToMany(mappedBy = "inventory", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH }, orphanRemoval = true)
    private Set<InventoryItem> inventoryItems = new LinkedHashSet<>();

    public Inventory() {
    }

    public Inventory(Integer inventoryId, Integer quantity, String diffReason, String inventoryStatus,
            LocalDateTime checkAt, LocalDateTime endAt, Admin admin, Set<InventoryItem> inventoryItems) {
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.diffReason = diffReason;
        this.inventoryStatus = inventoryStatus;
        this.checkAt = checkAt;
        this.endAt = endAt;
        this.admin = admin;
        this.inventoryItems = inventoryItems;
    }

    @Override
    public String toString() {
        return "InventoryBean [inventoryId=" + inventoryId + ", quantity=" + quantity + ", diffReason=" + diffReason
                + ", inventoryStatus=" + inventoryStatus + ", checkAt=" + checkAt + ", endAt=" + endAt + ", admin="
                + admin + ", inventoryItems=" + inventoryItems + "]";
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDiffReason() {
        return diffReason;
    }

    public void setDiffReason(String diffReason) {
        this.diffReason = diffReason;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public LocalDateTime getCheckAt() {
        return checkAt;
    }

    public void setCheckAt(LocalDateTime checkAt) {
        this.checkAt = checkAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Set<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(Set<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

}
