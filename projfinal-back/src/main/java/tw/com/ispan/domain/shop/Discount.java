package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Admin;

@Entity
@Table(name = "Discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountId;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;

    @Column(nullable = false)
    private LocalDateTime discountStartTime;

    @Column(nullable = false)
    private LocalDateTime discountEndTime;

    @Column
    private Double minAmount; // Nullable by default

    @Column
    private Integer minQuantity; // Nullable by default

    @Column
    private Integer priority; // Nullable by default

    @Column(length = 20)
    private String discountStatus; // Nullable by default, can add length constraint

    @Column(length = 20)
    private String discountType; // Nullable by default, can add length constraint

    @Column
    private Double discountValue; // Nullable by default

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountScope> discountScopes;

    // Constructors, getters, setters, toString()
}
