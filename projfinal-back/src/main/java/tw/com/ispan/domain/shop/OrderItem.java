package tw.com.ispan.domain.shop;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference  // Prevent infinite recursion when serializing
    private Orders order;  // Link back to the Orders entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Link to Product entity

    @Column(nullable = false)
    private Integer orderQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasedPrice;

    @Column(nullable = false)
    private String status;

    // Default constructor
    public OrderItem() {}

    // Constructor with all fields
    public OrderItem(Integer orderItemId, Orders order, Product product, Integer orderQuantity,
                     BigDecimal purchasedPrice, String status) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.product = product;
        this.orderQuantity = orderQuantity;
        this.purchasedPrice = purchasedPrice;
        this.status = status;
    }

    // Getters and Setters
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

    // Helper methods to set the foreign keys
    public void setOrderId(Integer orderId) {
        if (this.order == null) {
            this.order = new Orders();
        }
        this.order.setOrderId(orderId);
    }

    public void setProductId(Integer productId) {
        if (this.product == null) {
            this.product = new Product();
        }
        this.product.setProductId(productId);
    }

    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + 
               ", order=" + (order != null ? order.getOrderId() : "null") +
               ", product=" + (product != null ? product.getProductId() : "null") +
               ", orderQuantity=" + orderQuantity + 
               ", purchasedPrice=" + purchasedPrice + 
               ", status=" + status + "]";
    }
}
