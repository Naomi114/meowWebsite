package tw.com.ispan.domain.pet;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "DistinctArea")
public class DistinctArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer distinctAreaId;

	@Column(columnDefinition = "NVARCHAR(5)", name = "distinctAreaName", nullable = false)
	private String distinctAreaName;

	// 和RescueCase表雙向一對多
	@OneToMany(mappedBy = "distinctArea", cascade = CascadeType.PERSIST)
	private List<RescueCase> rescueCases;
	
	// 和LostCase表雙向一對多
	@OneToMany(mappedBy = "distinctArea", cascade = CascadeType.PERSIST)
	private List<LostCase> lostCases;
	
	// 和adoptionCase表雙向一對多
	@OneToMany(mappedBy = "distinctArea", cascade = CascadeType.PERSIST)
	private List<AdoptionCase> adoptionCase;
	
	

	public DistinctArea() {
		super();
	}

	public DistinctArea(Integer distinctAreaId, String distinctAreaName) {
		super();
		this.distinctAreaId = distinctAreaId;
		this.distinctAreaName = distinctAreaName;
	}

	public Integer getDistinctAreaId() {
		return distinctAreaId;
	}

	public void setDistinctAreaId(int distinctAreaId) {
		this.distinctAreaId = distinctAreaId;
	}

	public String getDistinctAreaName() {
		return distinctAreaName;
	}

	public void setDistinctAreaName(String distinctAreaName) {
		this.distinctAreaName = distinctAreaName;
	}

	public List<RescueCase> getRescueCases() {
		return rescueCases;
	}

	public void setRescueCases(List<RescueCase> rescueCases) {
		this.rescueCases = rescueCases;
	}

}
