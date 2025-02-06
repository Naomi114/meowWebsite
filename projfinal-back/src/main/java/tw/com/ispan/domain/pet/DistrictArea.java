package tw.com.ispan.domain.pet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "DistrictArea")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "districtAreaId")
public class DistrictArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer districtAreaId;

	@Column(columnDefinition = "NVARCHAR(5)", name = "districtAreaName", nullable = false)
	private String districtAreaName;

	// 和City表雙向多對一
	@ManyToOne(optional = false)
	@JoinColumn(name = "cityId", nullable = true, foreignKey = @ForeignKey(name = "FK_DistrictAreas_City"))
	private City city;

	// 和RescueCase表雙向一對多
	@OneToMany(mappedBy = "districtArea", cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<RescueCase> rescueCases;

	// 和LostCase表雙向一對多
	@OneToMany(mappedBy = "districtArea", cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<LostCase> lostCases;

	// 和adoptionCase表雙向一對多
	@OneToMany(mappedBy = "districtArea", cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<AdoptionCase> adoptionCase;

	public DistrictArea() {
		super();
	}

	public DistrictArea(Integer districtAreaId, String districtAreaName) {
		super();
		this.districtAreaId = districtAreaId;
		this.districtAreaName = districtAreaName;
	}

	public Integer getDistrictAreaId() {
		return districtAreaId;
	}

	public void setDistrictAreaId(int districtAreaId) {
		this.districtAreaId = districtAreaId;
	}

	public String getDistrictAreaName() {
		return districtAreaName;
	}

	public void setDistrictAreaName(String districtAreaName) {
		this.districtAreaName = districtAreaName;
	}
	
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<RescueCase> getRescueCases() {
		return rescueCases;
	}

	public void setRescueCases(List<RescueCase> rescueCases) {
		this.rescueCases = rescueCases;
	}

}
