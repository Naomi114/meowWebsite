package tw.com.ispan.domain.shop;

import com.fasterxml.jackson.annotation.JsonBackReference; // Ensure this import
import com.fasterxml.jackson.annotation.JsonManagedReference; // Ensure this import
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import tw.com.ispan.domain.shop.Discount;
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
    @JsonProperty("cartItemId") // Ensure JSON parsing includes cartItemId
    private Integer cartItemId;

    // Many-to-One relationship with Cart (Back reference to prevent circular
    // dependencies)
    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    @JsonBackReference("cartItems") // Unique back reference name for Cart
    private Cart cart;

    // One-to-One relationship with Product
    @OneToOne
    @JoinColumn(name = "product_Id", nullable = false)
    @JsonManagedReference("cartItem") // Managed side for serialization
    private Product product;

    // Many-to-One relationship with Orders (Avoid circular reference with
    // @JsonBackReference)
    @ManyToOne
    @JoinColumn(name = "orderId") // Assuming this is the order ID
    @JsonBackReference("cart-items-order") // Unique back reference name for Orders
    private Orders order;

    // Many-to-One relationship with Discount (Ensure this field exists)
    @ManyToOne
    @JoinColumn(name = "discountId") // This should match the FK in your database
    @JsonBackReference("cart-items-discount") // This is the back reference name for Discount
    private Discount discount;

    @Column(name = "cartItemStatus")
    @JsonProperty("cartItemStatus") // Ensure JSON field maps correctly
    private String cartItemStatus;

    @Column(name = "createDate")
    @JsonProperty("createDate") // Ensure JSON field maps correctly
    private LocalDateTime createDate;

    @Column(name = "updateDate")
    @JsonProperty("updateDate") // Ensure JSON field maps correctly
    private LocalDateTime updateDate;

    @Column(name = "cartItemQuantity", nullable = false)
    @JsonProperty("quantity") // Map 'cartItemQuantity' to 'quantity' in JSON
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

    // Additional Methods for Handling Product Fields

    @JsonProperty("quantity") // Ensure this maps to 'quantity' in JSON
    public Integer getQuantity() {
        return this.cartItemQuantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.cartItemQuantity = quantity;
    }

    // Set the product name if the product is present
    public void setProductName(String productName) {
        if (this.product != null) {
            this.product.setProductName(productName);
        }
    }

    // Set the sale price if the product is present
    public void setSalePrice(Double salePrice) {
        if (this.product != null) {
            this.product.setSalePrice(BigDecimal.valueOf(salePrice));
        }
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.updateDate = lastUpdatedDate;
    }
}
