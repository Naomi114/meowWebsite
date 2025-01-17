package tw.com.ispan.domain.pet;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "FurColor")
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

    
    
    //constructor
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
}
