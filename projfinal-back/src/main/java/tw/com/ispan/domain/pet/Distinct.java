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
@Table(name = "Distinct")
public class Distinct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer distinctId;

	@Column(columnDefinition = "NVARCHAR(5)", name = "distinctName", nullable = false)
	private String distinctName;

	// 和RescueCase表雙向一對多
	@OneToMany(mappedBy = "distinctId", cascade = CascadeType.PERSIST)
	private List<RescueCase> rescueCases;

	public Distinct() {
		super();
	}

	public Distinct(Integer distinctId, String distinctName) {
		super();
		this.distinctId = distinctId;
		this.distinctName = distinctName;
	}

	public Integer getDistinctId() {
		return distinctId;
	}

	public void setDistinctId(int distinctId) {
		this.distinctId = distinctId;
	}

	public String getDistinctName() {
		return distinctName;
	}

	public void setDistinctName(String distinctName) {
		this.distinctName = distinctName;
	}

	public List<RescueCase> getRescueCases() {
		return rescueCases;
	}

	public void setRescueCases(List<RescueCase> rescueCases) {
		this.rescueCases = rescueCases;
	}

}
