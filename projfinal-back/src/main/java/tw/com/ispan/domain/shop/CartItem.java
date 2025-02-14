package tw.com.ispan.domain.shop;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonProperty("cartItemId") // 確保 JSON 解析包含 cartItemId
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "FK_cartId", nullable = false)
    @JsonProperty("cartId")
    @JsonBackReference("cartItems") // Unique back reference name for Cart
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "FK_productId", nullable = false)
    @JsonProperty("product_Id")
    @JsonManagedReference("cartItem") // Managed side for serialization
    private Product product;

    @ManyToOne
    @JoinColumn(name = "FK_orderId") // Assuming this is the order ID
    @JsonProperty("orderId")
    @JsonBackReference("cart-items-order") // Unique back reference name for Orders
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "FK_discountId") // This should match the FK in your database
    @JsonProperty("discountI")
    @JsonBackReference("cart-items-discount") // This is the back reference name for Discount
    private Discount discount;

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
    @JsonProperty("quantity") // JSON 解析對應 quantity
    private Integer cartItemQuantity;

    // ✅ 修正 `getId()`，確保返回 cartItemId
    public Integer getId() {
        return cartItemId;
    }

    // Setters 和 Getters
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

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
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
