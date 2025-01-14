package tw.com.ispan.domain.pet.forAdopt;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Join;

@Entity
@Table(name = "adoptioncaseapply")
public class AdoptionCaseApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adoptionCaseApplyId;

    @Column(nullable = false, columnDefinition = "nvarchar(max)")
    private String introduction;

    @Column(nullable = false)
    private boolean applicationStatus;

    // 雙向多對一,外鍵在多方
    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private AdoptionCase adoptionCase;

    @ManyToMany(mappedBy = "adoptioncaseapply")
    private Set<AdoptionCase> AdoptionCase;

}
