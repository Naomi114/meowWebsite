package tw.com.ispan.projfinal_back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
    private Category category;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductBean product;

    @ManyToOne
    @JoinColumn(name = "birthdayMonth")
    private Member member;

    // Constructors, getters, setters, toString()
}
