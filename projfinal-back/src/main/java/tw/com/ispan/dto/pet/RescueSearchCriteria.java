package tw.com.ispan.dto.pet;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public class RescueSearchCriteria {

	private String keyword; // 關鍵字
	
	@Min(value = 1, message = "案件狀態ID不得小於1")
	@Max(value = 9, message = "案件狀態ID不得大於9")
	@Positive(message = "案件狀態ID必須為正數")
	private Integer caseStateId; // 救援狀態
	
	@Min(value = 1, message = "縣市id不得小於1")
	@Max(value = 24, message = "縣市id不得大於24")
	@Positive(message = "縣市ID必須為正數")
	private Integer cityId; // 縣市
	
	@Min(value = 1, message = "區域id不得小於1")
	@Max(value = 374, message = "區域id不得大於374")
	private Integer districtAreaId; // 鄉鎮區
	
	@Min(value = 1, message = "物種id不能小於1")
	@Max(value = 2, message = "物種id不能大於2")
	@Positive(message = "物種ID必須為正數")
	private Integer speciesId; // 物種
	
	@Min(value = 1, message = "品種id不能小於1")
	@Max(value = 186, message = "品種id不能大於186")
	private Integer breedId; // 品種
	
	@Min(value = 1, message = "毛色id不能小於 1")
	@Max(value = 7, message = "毛色id不能大於 7")
	private Integer furColorId;  //毛色
	
	private Boolean suspectLost; // 走失標記 (true/false)

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getCaseStateId() {
		return caseStateId;
	}

	public void setCaseStateId(Integer caseStateIdId) {
		this.caseStateId = caseStateIdId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictAreaId() {
		return districtAreaId;
	}

	public void setDistrictAreaId(Integer districtAreaId) {
		this.districtAreaId = districtAreaId;
	}

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

	public Integer getBreedId() {
		return breedId;
	}

	public void setBreedId(Integer breedId) {
		this.breedId = breedId;
	}
	

	public Integer getFurColorId() {
		return furColorId;
	}

	public void setFurColorId(Integer furColorId) {
		this.furColorId = furColorId;
	}

	public Boolean getSuspectLost() {
		return suspectLost;
	}

	public void setSuspectLost(Boolean suspectLost) {
		this.suspectLost = suspectLost;
	}

	@Override
	public String toString() {
		return "RescueSearchCriteria [keyword=" + keyword + ", caseStateId=" + caseStateId + ", cityId=" + cityId
				+ ", districtAreaId=" + districtAreaId + ", speciesId=" + speciesId + ", breedId=" + breedId
				+ ", furColorId=" + furColorId + ", suspectLost=" + suspectLost + "]";
	}

	

	

}
