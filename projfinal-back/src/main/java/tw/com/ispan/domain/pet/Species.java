package tw.com.ispan.domain.pet;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speciesId")
    private Integer speciesId;

    @Column(name = "species", nullable = false, length = 10)
    private String species;

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
    private List<LostCase> lostCases;

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
    private List<RescueCase> rescueCases;
    

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
    private List<AdoptionCase> adoptionCase;	

    
    
    public Species() {
		super();
	}

	

	public Species(String species) {
		super();
		this.species = species;
	}



	// Getters and Setters
    public Integer getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
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