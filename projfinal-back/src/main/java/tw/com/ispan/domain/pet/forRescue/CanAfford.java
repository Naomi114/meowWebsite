package tw.com.ispan.domain.pet.forRescue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CanAfford")
public class CanAfford {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "canAffordId")
	private Integer canAffordId;

	@Column(name = "canAfford", columnDefinition = "NVARCHAR(10)")
	private String canAfford;

	public CanAfford() {
		super();
	}

	public CanAfford(String canAfford) {
		super();
		this.canAfford = canAfford;
	}

	public Integer getCanAffordId() {
		return canAffordId;
	}

	public void setCanAffordId(Integer canAffordId) {
		this.canAffordId = canAffordId;
	}

	public String getCanAfford() {
		return canAfford;
	}

	public void setCanAfford(String canAfford) {
		this.canAfford = canAfford;
	}

	@Override
	public String toString() {
		return "CanAfford [canAffordId=" + canAffordId + ", canAfford=" + canAfford + "]";
	}

}
