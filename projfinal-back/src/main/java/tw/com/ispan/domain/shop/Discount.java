package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;

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

    private Double minAmount;

    private Integer minQuantity;

    private Integer priority;

    private String discountStatus;

    private String discountType;

    private Double discountValue;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountScope> discountScopes;

    // Constructors, getters, setters, toString()
}
