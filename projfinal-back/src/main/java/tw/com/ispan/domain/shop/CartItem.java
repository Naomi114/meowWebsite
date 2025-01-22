package tw.com.ispan.domain.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cartitem")
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    @JsonProperty("cartItemId") // 確保 JSON 解析
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    @JsonIgnore // 避免循環依賴導致的 JSON 序列化錯誤
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderId")
    @JsonIgnore // 避免循環依賴
    private Orders order;

    @Column(name = "cartItemStatus")
    @JsonProperty("cartItemStatus")
    private String cartItemStatus;

    @Column(name = "createDate")
    @JsonProperty("createDate")
    private LocalDateTime createDate;

    @Column(name = "updateDate")
    @JsonProperty("updateDate")
    private LocalDateTime updateDate;

    @Column(name = "cartItemQuantity", nullable = false)
    @JsonProperty("quantity")
    private Integer cartItemQuantity;

    // Getters and Setters

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public String getCartItemStatus() {
        return cartItemStatus;
    }

    public void setCartItemStatus(String cartItemStatus) {
        this.cartItemStatus = cartItemStatus;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    // Additional Methods for Handling Product Fields

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return this.cartItemQuantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.cartItemQuantity = quantity;
    }

    public void setProductName(String productName) {
        if (this.product != null) {
            this.product.setProductName(productName);
        }
    }

    public void setSalePrice(Double salePrice) {
        if (this.product != null) {
            this.product.setSalePrice(BigDecimal.valueOf(salePrice));
        }
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.updateDate = lastUpdatedDate;
    }
}
