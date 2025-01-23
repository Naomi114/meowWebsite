package tw.com.ispan.domain.shop;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "OrderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Orders order; // This is a reference to the Order object, not just an orderId

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product; // This is a reference to the Product object, not just a productId

    @Column(nullable = false)
    private Integer orderQuantity;

    @Column(nullable = false)
    private BigDecimal purchasedPrice;

    @Column(nullable = false)
    private String status; // Added in 2025-01-14 Naomi (ref. InventoryService)

    public OrderItem() {
    }

    public OrderItem(Integer orderItemId, Orders order, Product product, Integer orderQuantity,
    BigDecimal purchasedPrice, String status) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.product = product;
        this.orderQuantity = orderQuantity;
        this.purchasedPrice = purchasedPrice;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + ", order=" + order + ", product=" + product
                + ", orderQuantity=" + orderQuantity + ", purchasedPrice=" + purchasedPrice + ", status=" + status
                + "]";
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(BigDecimal purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Added setter methods for orderId and productId
    public void setOrderId(Integer orderId) {
        if (this.order == null) {
            this.order = new Orders(); // Initialize the order object if it's null
        }
        this.order.setOrderId(orderId); // Assuming Orders has a setOrderId method
    }

    public void setProductId(Integer productId) {
        if (this.product == null) {
            this.product = new Product(); // Initialize the product object if it's null
        }
        this.product.setProductId(productId); // Assuming Product has a setProductId method
    }
}
