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
@Table(name = "Distint")
public class Distint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer distintId;

	@Column(columnDefinition = "NVARCHAR(5)", name = "distintName", nullable = false)
	private String distintName;

	// 和RescueCase表雙向一對多
	@OneToMany(mappedBy = "distintId", cascade = CascadeType.PERSIST)
	private List<RescueCase> rescueCases;

	public Distint() {
		super();
	}

	public Distint(Integer distintId, String distintName) {
		super();
		this.distintId = distintId;
		this.distintName = distintName;
	}

	public Integer getDistintId() {
		return distintId;
	}

	public void setDistintId(int distintId) {
		this.distintId = distintId;
	}

	public String getDistintName() {
		return distintName;
	}

	public void setDistintName(String distintName) {
		this.distintName = distintName;
	}

	public List<RescueCase> getRescueCases() {
		return rescueCases;
	}

	public void setRescueCases(List<RescueCase> rescueCases) {
		this.rescueCases = rescueCases;
	}

}
