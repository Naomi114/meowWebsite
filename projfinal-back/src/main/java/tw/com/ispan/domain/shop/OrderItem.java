package tw.com.ispan.domain.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductBean product;

    @Column(nullable = false)
    private Integer orderQuantity;

    @Column(nullable = false)
    private Double purchasedPrice;

    @Column(nullable = false)
    private String status; // 20250114 Naomi 新增 (ref. InventoryService)

    public OrderItem() {
    }

    public OrderItem(Integer orderItemId, Order order, ProductBean product, Integer orderQuantity,
            Double purchasedPrice) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.product = product;
        this.orderQuantity = orderQuantity;
        this.purchasedPrice = purchasedPrice;
    }

    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + ", order=" + order + ", product=" + product
                + ", orderQuantity=" + orderQuantity + ", purchasedPrice=" + purchasedPrice + "]";
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Double getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(Double purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
