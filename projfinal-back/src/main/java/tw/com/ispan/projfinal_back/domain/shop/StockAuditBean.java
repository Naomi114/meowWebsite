package tw.com.ispan.projfinal_back.domain.shop;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stockAudit")
public class StockAuditBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditId;

    @Column(nullable = false)
    private Date auditDate;

    @Column(nullable = false)
    private Integer quantityBefore;

    @Column(nullable = false)
    private Integer quantityAfter;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductBean product;

    @ManyToOne
    @JoinColumn(name = "inventoryId", nullable = false)
    private InventoryBean inventory;

    public StockAuditBean() {
    }

    public StockAuditBean(Integer auditId, Date auditDate, Integer quantityBefore, Integer quantityAfter,
            ProductBean product, InventoryBean inventory) {
        this.auditId = auditId;
        this.auditDate = auditDate;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.product = product;
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "StockAudit [auditId=" + auditId + ", auditDate=" + auditDate + ", quantityBefore=" + quantityBefore
                + ", quantityAfter=" + quantityAfter + ", product=" + product + ", inventory=" + inventory + "]";
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getQuantityBefore() {
        return quantityBefore;
    }

    public void setQuantityBefore(Integer quantityBefore) {
        this.quantityBefore = quantityBefore;
    }

    public Integer getQuantityAfter() {
        return quantityAfter;
    }

    public void setQuantityAfter(Integer quantityAfter) {
        this.quantityAfter = quantityAfter;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public InventoryBean getInventory() {
        return inventory;
    }

    public void setInventory(InventoryBean inventory) {
        this.inventory = inventory;
    }

    
}

