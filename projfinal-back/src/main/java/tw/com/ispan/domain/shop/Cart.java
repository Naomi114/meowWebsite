package tw.com.ispan.domain.shop;

import jakarta.persistence.*;
import tw.com.ispan.domain.admin.Member;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cart") // 資料庫表名
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId") // 明確指定資料庫中的列名
    private Long cartId; // 主鍵，Long 類型

    @ManyToOne // 修正為 ManyToOne 關聯，適合一個會員多筆購物車記錄
    @JoinColumn(name = "memberId", nullable = false) // 外鍵關聯，指定 memberId 參照 Member 實體
    private Member member;

    @Column(name = "lastUpdatedDate", nullable = false) // 更新日期
    private LocalDateTime lastUpdatedDate;

    @Column(name = "productId", nullable = false) // 商品 ID
    private Integer productId;

    @Column(name = "productName", nullable = false) // 商品名稱
    private String productName;

    @Column(name = "salePrice", nullable = false) // 商品價格
    private Double salePrice;

    @Column(name = "quantity", nullable = false) // 商品數量
    private Integer quantity;

    @Transient // 用於表示不存儲在資料庫中的屬性
    private Boolean selected; // 是否選擇商品（用於前端顯示）

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
