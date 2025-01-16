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

    public AdoptionCaseApply(Integer adoptionCaseApplyId, String introduction, boolean applicationStatus,
            Set<AdoptionCase> adoptionCase) {
        this.adoptionCaseApplyId = adoptionCaseApplyId;
        this.introduction = introduction;
        this.applicationStatus = applicationStatus;
        this.adoptionCase = adoptionCase;
    }

    public Integer getAdoptionCaseApplyId() {
        return adoptionCaseApplyId;
    }

    public void setAdoptionCaseApplyId(Integer adoptionCaseApplyId) {
        this.adoptionCaseApplyId = adoptionCaseApplyId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public boolean isApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(boolean applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Set<AdoptionCase> getAdoptionCase() {
        return adoptionCase;
    }

    public void setAdoptionCase(Set<AdoptionCase> adoptionCase) {
        this.adoptionCase = adoptionCase;
    }

    @Override
    public String toString() {
        return "AdoptionCaseApply [adoptionCaseApplyId=" + adoptionCaseApplyId + ", introduction=" + introduction
                + ", applicationStatus=" + applicationStatus + ", adoptionCase=" + adoptionCase
                + ", getAdoptionCaseApplyId()=" + getAdoptionCaseApplyId() + ", getIntroduction()=" + getIntroduction()
                + ", isApplicationStatus()=" + isApplicationStatus() + ", getAdoptionCase()=" + getAdoptionCase()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }

}
