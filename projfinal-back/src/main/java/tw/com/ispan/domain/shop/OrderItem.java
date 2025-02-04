package tw.com.ispan.domain.shop;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;  // 訂單項目 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference  // 防止序列化時的循環引用
    private Orders order;  // 關聯到 Orders

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // 避免 Hibernate 懶加載問題
    private Product product;  // 關聯到商品

    @Column(nullable = false)
    private Integer orderQuantity;  // 訂購數量

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasedPrice;  // 當前購買時的價格

    @Column(nullable = false)
    private String status;  // 訂單狀態（如："備貨中"、"已發貨"）

    // **新增方法**：提供商品名稱，確保前端可以獲取
    @JsonProperty("productName")
    public String getProductName() {
        return (product != null) ? product.getProductName() : "未知商品";
    }

    // 預設建構子
    public OrderItem() {}

    // 全部欄位建構子
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

    // 設定 orderId（輔助方法）
    public void setOrderId(Integer orderId) {
        if (this.order == null) {
            this.order = new Orders();
        }
        this.order.setOrderId(orderId);
    }

    // 設定 productId（輔助方法）
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
               ", productName=" + getProductName() +
               ", orderQuantity=" + orderQuantity + 
               ", purchasedPrice=" + purchasedPrice + 
               ", status=" + status + "]";
    }
}
