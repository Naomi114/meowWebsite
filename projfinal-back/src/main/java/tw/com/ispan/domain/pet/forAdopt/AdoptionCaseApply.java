package tw.com.ispan.domain.pet.forAdopt;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.pet.AdoptionCase;

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

    // 雙向多對多,外鍵在多方
    @ManyToMany(mappedBy = "adoptioncaseapply")
    private Set<AdoptionCase> adoptionCase;

}
