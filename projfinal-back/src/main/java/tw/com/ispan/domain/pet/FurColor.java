package tw.com.ispan.domain.pet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "FurColor")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "furColorId")                                                                                           // 作為唯一標識符

public class FurColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furColorId")
    private Integer furColorId;

    @Column(name = "furColor", nullable = false, length = 20)
    private String furColor;

    @OneToMany(mappedBy = "furColor", cascade = CascadeType.ALL)
    private List<LostCase> lostCases;

    @OneToMany(mappedBy = "furColor", cascade = CascadeType.ALL)
    private List<RescueCase> rescueCases;

    @OneToMany(mappedBy = "furColor", cascade = CascadeType.ALL)
    private List<AdoptionCase> adoptionCase;

    // constructor
    public FurColor() {
        super();
    }

    public FurColor(String furColor) {
        super();
        this.furColor = furColor;
    }

    // Getters and Setters
    public Integer getFurColorId() {
        return furColorId;
    }

    public void setFurColorId(Integer furColorId) {
        this.furColorId = furColorId;
    }

    public String getFurColor() {
        return furColor;
    }

    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }

    public List<RescueCase> getRescueCases() {
        return rescueCases;
    }

    public void setRescueCases(List<RescueCase> rescueCases) {
        this.rescueCases = rescueCases;
    }

    public List<LostCase> getLostCases() {
        return lostCases;
    }

    public void setLostCases(List<LostCase> lostCases) {
        this.lostCases = lostCases;
    }

    public List<AdoptionCase> getAdoptionCase() {
        return adoptionCase;
    }

    public void setAdoptionCase(List<AdoptionCase> adoptionCase) {
        this.adoptionCase = adoptionCase;
    }

    @Override
    public String toString() {
        return "FurColor [furColorId=" + furColorId + ", furColor=" + furColor + ", lostCases=" + lostCases
                + ", rescueCases=" + rescueCases + ", adoptionCase=" + adoptionCase + "]";
    }

}
