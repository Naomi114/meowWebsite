package tw.com.ispan.domain.pet;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "City")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cityId;

	@Column(name = "city", columnDefinition = "NVARCHAR(5)", nullable = false)
	private String city;

	// 和DistinctArea表單向一對多
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "city_id")
	private List<DistinctArea> distinctAreas;

	// 和RescueCase表雙向一對多
	@OneToMany(mappedBy = "city", cascade = CascadeType.PERSIST)
	private List<RescueCase> rescueCases;

	// 和LostCase表雙向一對多
	@OneToMany(mappedBy = "city", cascade = CascadeType.PERSIST)
	private List<LostCase> lostCases;

	// 和adoptionCase表雙向一對多
	@OneToMany(mappedBy = "city", cascade = CascadeType.PERSIST)
	private List<AdoptionCase> adoptionCases;

	public City() {
		super();
	}

	public City(String city) {
		super();
		this.city = city;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<RescueCase> getRescueCases() {
		return rescueCases;
	}

	public void setRescueCases(List<RescueCase> rescueCases) {
		this.rescueCases = rescueCases;
	}

	public List<DistinctArea> getDistinctAreas() {
		return distinctAreas;
	}

	public void setDistinctAreas(List<DistinctArea> distinctAreas) {
		this.distinctAreas = distinctAreas;
	}

	public List<LostCase> getLostCases() {
		return lostCases;
	}

	public void setLostCases(List<LostCase> lostCases) {
		this.lostCases = lostCases;
	}

	public List<AdoptionCase> getAdoptionCases() {
		return adoptionCases;
	}

	public void setAdoptionCases(List<AdoptionCase> adoptionCases) {
		this.adoptionCases = adoptionCases;
	}
	
	

}
