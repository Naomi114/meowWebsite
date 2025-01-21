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
@Table(name = "Species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speciesId")
    private Integer speciesId;

    @Column(name = "species", nullable = false, length = 10)
    private String species;

//改為單向
//    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
//    @JsonBackReference("lostCases-species")
//    private List<LostCase> lostCases;
//
//    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
//    @JsonBackReference("rescueCase-species")
//    private List<RescueCase> rescueCases;
//    
//    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
//    @JsonBackReference("adoptionCase-species")
//    private List<AdoptionCase> adoptionCase;	

    
    
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

    
}