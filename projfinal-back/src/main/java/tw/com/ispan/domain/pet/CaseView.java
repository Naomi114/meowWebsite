package tw.com.ispan.domain.pet;

import java.security.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "caseViews")
public class CaseView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rescueCaseId", nullable = true)
    private RescueCase rescueCase;

    @ManyToOne
    @JoinColumn(name = "lostCaseId", nullable = true)
    private LostCase lostCase;

    @ManyToOne
    @JoinColumn(name = "adoptionCaseId", nullable = true)
    private AdoptionCase adoptionCase;

    @Column(nullable = false)
    private LocalDateTime viewTime = LocalDateTime.now();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RescueCase getRescueCase() {
        return rescueCase;
    }

    public void setRescueCase(RescueCase rescueCase) {
        this.rescueCase = rescueCase;
    }

    public LostCase getLostCase() {
        return lostCase;
    }

    public void setLostCase(LostCase lostCase) {
        this.lostCase = lostCase;
    }

    public AdoptionCase getAdoptionCase() {
        return adoptionCase;
    }

    public void setAdoptionCase(AdoptionCase adoptionCase) {
        this.adoptionCase = adoptionCase;
    }

    public LocalDateTime getViewTime() {
        return viewTime;
    }

    public void setViewTime(LocalDateTime viewTime) {
        this.viewTime = viewTime;
    }

}
