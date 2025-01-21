package tw.com.ispan.domain.pet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
public class FurColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furColorId")
    private Integer furColorId;

    @Column(name = "furColor", nullable = false, length = 20)
    private String furColor;

    //改為單向，一個毛色不須知道有哪些案件 
//    @OneToMany(mappedBy = "furColor", cascade = CascadeType.ALL)
//    private List<LostCase> lostCases;
//
//    @OneToMany(mappedBy = "furColor", cascade = CascadeType.ALL)
//    private List<RescueCase> rescueCases;
//    
//    @OneToMany(mappedBy = "furColor", cascade = CascadeType.ALL)
//    private List<AdoptionCase> adoptionCase;

    
    
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
}
