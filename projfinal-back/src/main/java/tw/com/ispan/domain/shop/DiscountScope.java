package tw.com.ispan.domain.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "DiscountScope")
public class DiscountScope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountScopeId;

    @Column(nullable = false, unique = true)
    private String discountScopeType;

    @ManyToOne
    @JoinColumn(name = "discountId", nullable = false)
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private ProductCategoryBean category;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductBean product;

    @ManyToOne
    @JoinColumn(name = "birthdayMonth")
    private Member member;

    public DiscountScope() {
    }

    public DiscountScope(Integer discountScopeId, String discountScopeType, Discount discount,
            ProductCategoryBean category, ProductBean product, Member member) {
        this.discountScopeId = discountScopeId;
        this.discountScopeType = discountScopeType;
        this.discount = discount;
        this.category = category;
        this.product = product;
        this.member = member;
    }

    @Override
    public String toString() {
        return "DiscountScope [discountScopeId=" + discountScopeId + ", discountScopeType=" + discountScopeType
                + ", discount=" + discount + ", category=" + category + ", product=" + product + ", member=" + member
                + "]";
    }

    public Integer getDiscountScopeId() {
        return discountScopeId;
    }

    public void setDiscountScopeId(Integer discountScopeId) {
        this.discountScopeId = discountScopeId;
    }

    public String getDiscountScopeType() {
        return discountScopeType;
    }

    public void setDiscountScopeType(String discountScopeType) {
        this.discountScopeType = discountScopeType;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public ProductCategoryBean getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryBean category) {
        this.category = category;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
