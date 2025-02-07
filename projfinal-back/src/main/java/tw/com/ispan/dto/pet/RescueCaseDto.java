package tw.com.ispan.dto.pet;

import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RescueCaseDto {

	// 接收使用者填的新增案件資料

	@NotNull(message = "案件標題必填")
	@Length(min = 1, max = 15) // 限制小於15字
	private String caseTitle;

	@Min(value = 1, message = "物種id不能小於1")
	@Max(value = 2, message = "物種id不能大於2")
	@Positive(message = "物種ID必須為正數")
	@NotNull(message = "物種必填")
	// 如果填的是固定值建議可用建立Enum類別與驗證
	private Integer speciesId;

	@Min(value = 1, message = "品種id不能小於1")
	@Max(value = 186, message = "品種id不能大於186")
	@NotNull(message = "品種必填")
	private Integer breedId;

	@Min(value = 1, message = "毛色id不能小於 1")
	@Max(value = 7, message = "毛色id不能大於 7")
	private Integer furColorId;

	@Pattern(regexp = "^(公|母)$", message = "性別必須為公或母")
	private String gender;

	@Pattern(regexp = "^(已絕育|未絕育)$", message = "絕育狀態必須為已絕育或未絕育")
	private String sterilization;

	@Min(value = 0, message = "年齡不能小於 0")
	@Max(value = 30, message = "年齡不能大於30") // 只能輸入0~30
	private Integer age;

	@DecimalMin("1000000000") // 晶片號碼為10位數字
	@DecimalMax("10000000000")
	private Integer microChipNumber;

	@NotNull(message = "是否遺失為必填")
	private Boolean suspLost;

	@Min(value = 1, message = "縣市id不得小於1")
	@Max(value = 24, message = "縣市id不得大於24")
	@Positive(message = "縣市ID必須為正數")
	@NotNull(message = "縣市為必填")
	private Integer cityId;

	@Min(value = 1, message = "區域id不得小於1")
	@Max(value = 374, message = "區域id不得大於374")
	@Positive(message = "區域ID必須為正數")
	@NotNull(message = "區域為必填")
	private Integer distinctAreaId;

	private String street;

	@NotNull(message = "區域為必填")
	private String rescueReason;

	@Min(value = 1, message = "案件狀態ID不得小於1")
	@Max(value = 9, message = "案件狀態ID不得大於9")
	@Positive(message = "案件狀態ID必須為正數")
	private Integer caseStateId; // 初次添加時使用者不會填所以可為null，但修改時則必填，應用到@validated分組驗證

	@Length(min = 0, max = 50) // 限制不得超過50字
	private String tag;

	// 前端傳遞照片暫存url給後端處理，因為前端只會有3個上傳檔案功能所以也只會收到3個url
	@NotEmpty(message = "照片為必填")
	@Size(min = 1, max = 3) // 至少一張，至多三張
	private List<String> casePictures;

	@NotEmpty(message = "救援需求為必填")
	private List<Integer> rescueDemands;

	@NotEmpty(message = "可負擔資助為必填")
	private List<Integer> canAffords;

	// getter & setter
	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSterilization() {
		return sterilization;
	}

	public void setSterilization(String sterilization) {
		this.sterilization = sterilization;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getMicroChipNumber() {
		return microChipNumber;
	}

	public void setMicroChipNumber(Integer microChipNumber) {
		this.microChipNumber = microChipNumber;
	}

	public Boolean getSuspLost() {
		return suspLost;
	}

	public void setSuspLost(Boolean suspLost) {
		this.suspLost = suspLost;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistinctAreaId() {
		return distinctAreaId;
	}

	public void setDistinctAreaId(Integer distinctAreaId) {
		this.distinctAreaId = distinctAreaId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getRescueReason() {
		return rescueReason;
	}

	public void setRescueReason(String rescueReason) {
		this.rescueReason = rescueReason;
	}

	public List<String> getCasePictures() {
		return casePictures;
	}

	public void setCasePictures(List<String> casePictures) {
		this.casePictures = casePictures;
	}

	public List<Integer> getRescueDemands() {
		return rescueDemands;
	}

	public void setRescueDemands(List<Integer> rescueDemands) {
		this.rescueDemands = rescueDemands;
	}

	public List<Integer> getCanAffords() {
		return canAffords;
	}

	public void setCanAffords(List<Integer> canAffords) {
		this.canAffords = canAffords;
	}

	public Integer getCaseStateId() {
		return caseStateId;
	}

	public void setCaseStateId(Integer caseStateId) {
		this.caseStateId = caseStateId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "RescueCaseDto [caseTitle=" + caseTitle + ", speciesId=" + speciesId + ", breedId=" + breedId
				+ ", furColorId=" + furColorId + ", gender=" + gender + ", sterilization=" + sterilization + ", age="
				+ age + ", microChipNumber=" + microChipNumber + ", suspLost=" + suspLost + ", cityId=" + cityId
				+ ", distinctAreaId=" + distinctAreaId + ", street=" + street + ", rescueReason=" + rescueReason
				+ ", caseStateId=" + caseStateId + ", tag=" + tag + ", casePictures=" + casePictures
				+ ", rescueDemands=" + rescueDemands + ", canAffords=" + canAffords + "]";
	}

}
