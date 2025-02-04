package tw.com.ispan.domain.pet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "districtAreaId" // 使用
																										// districtAreaId
																										// 作為唯一標識符
)
public class DistrictArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer districtAreaId;

	@Column(columnDefinition = "NVARCHAR(5)", name = "districtAreaName", nullable = false)
	private String districtAreaName;

	@ManyToOne
	@JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_DistrictArea_City"))
	private City city;

	// 和RescueCase表雙向一對多
	@OneToMany(mappedBy = "districtArea", cascade = CascadeType.PERSIST)
	private List<RescueCase> rescueCases;

	// 和LostCase表雙向一對多
	@OneToMany(mappedBy = "districtArea", cascade = CascadeType.PERSIST)
	private List<LostCase> lostCases;

	// 和adoptionCase表雙向一對多
	@OneToMany(mappedBy = "districtArea", cascade = CascadeType.PERSIST)
	private List<AdoptionCase> adoptionCase;

	public DistrictArea() {
		super();
	}

	public DistrictArea(Integer districtAreaId, String districtAreaName) {
		super();
		this.districtAreaId = districtAreaId;
		this.districtAreaName = districtAreaName;
	}

	public Integer getDistinctAreaId() {
		return districtAreaId;
	}

	public void setDistinctAreaId(Integer districtAreaId) {
		this.districtAreaId = districtAreaId;
	}

	public String getDistinctAreaName() {
		return districtAreaName;
	}

	public void setDistinctAreaName(String districtAreaName) {
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
		return "DistrictArea [districtAreaId=" + districtAreaId + ", districtAreaName=" + districtAreaName + "]";
	}

}
