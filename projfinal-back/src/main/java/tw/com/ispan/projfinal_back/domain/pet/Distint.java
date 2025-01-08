package tw.com.ispan.projfinal_back.domain.pet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="distint")
@Entity(name="Distint")
public class Distint {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int casePictureId ;
	
	private int rescueCaseId;
	
	private int lostCaseId;
	
	private int adoptionCaseId;
	
	private String pictureUrl;

	
	public Distint() {
		super();
	}


	public Distint(int casePictureId, int rescueCaseId, int lostCaseId, int adoptionCaseId, String pictureUrl) {
		super();
		this.casePictureId = casePictureId;
		this.rescueCaseId = rescueCaseId;
		this.lostCaseId = lostCaseId;
		this.adoptionCaseId = adoptionCaseId;
		this.pictureUrl = pictureUrl;
	}


	public int getCasePictureId() {
		return casePictureId;
	}


	public void setCasePictureId(int casePictureId) {
		this.casePictureId = casePictureId;
	}


	public int getRescueCaseId() {
		return rescueCaseId;
	}


	public void setRescueCaseId(int rescueCaseId) {
		this.rescueCaseId = rescueCaseId;
	}


	public int getLostCaseId() {
		return lostCaseId;
	}


	public void setLostCaseId(int lostCaseId) {
		this.lostCaseId = lostCaseId;
	}


	public int getAdoptionCaseId() {
		return adoptionCaseId;
	}


	public void setAdoptionCaseId(int adoptionCaseId) {
		this.adoptionCaseId = adoptionCaseId;
	}


	public String getPictureUrl() {
		return pictureUrl;
	}


	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	
	
	
}
