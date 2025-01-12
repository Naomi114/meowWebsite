package tw.com.ispan.domain.pet.forRescue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RescueDemand")
public class RescueDemand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rescueDemandId")
	private Integer rescueDemandId;

	@Column(name = "rescueDemand", columnDefinition = "NVARCHAR(10)")
	private String rescueDemand;

	
	public RescueDemand() {
		super();
	}


	public RescueDemand(Integer rescueDemandId, String rescueDemand) {
		super();
		this.rescueDemandId = rescueDemandId;
		this.rescueDemand = rescueDemand;
	}


	public Integer getRescueDemandId() {
		return rescueDemandId;
	}


	public void setRescueDemandId(Integer rescueDemandId) {
		this.rescueDemandId = rescueDemandId;
	}


	public String getRescueDemand() {
		return rescueDemand;
	}


	public void setRescueDemand(String rescueDemand) {
		this.rescueDemand = rescueDemand;
	}


	@Override
	public String toString() {
		return "RescueDemand [rescueDemandId=" + rescueDemandId + ", rescueDemand=" + rescueDemand + "]";
	}



	
	
}
