package tw.com.ispan.projfinal_back.domain.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;

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

@Entity
@Table(name = "inventory")
public class InventoryBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;

    @Column(nullable = false)
    private Integer quantity;

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

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAuditBean> stockAudits;

    public InventoryBean() {
    }

    public InventoryBean(Integer inventoryId, Integer quantity, String diffReason, String inventoryStatus,
            LocalDateTime checkAt, LocalDateTime endAt, Admin admin, List<StockAuditBean> stockAudits) {
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.diffReason = diffReason;
        this.inventoryStatus = inventoryStatus;
        this.checkAt = checkAt;
        this.endAt = endAt;
        this.admin = admin;
        this.stockAudits = stockAudits;
    }

    @Override
    public String toString() {
        return "InventoryBean [inventoryId=" + inventoryId + ", quantity=" + quantity + ", diffReason=" + diffReason
                + ", inventoryStatus=" + inventoryStatus + ", checkAt=" + checkAt + ", endAt=" + endAt + ", admin="
                + admin + ", stockAudits=" + stockAudits + "]";
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

    public List<StockAuditBean> getStockAudits() {
        return stockAudits;
    }

    public void setStockAudits(List<StockAuditBean> stockAudits) {
        this.stockAudits = stockAudits;
    }

}
