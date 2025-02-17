package tw.com.ispan.dto.pet;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public class LostSearchCriteria {

    private String keyword; // 關鍵字
    private String gender;
    private String sterilization;
    private String microChipNumber;

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
    private Integer furColorId; // 毛色

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

    public String getMicroChipNumber() {
        return microChipNumber;
    }

    public void setMicroChipNumber(String microChipNumber) {
        this.microChipNumber = microChipNumber;
    }

    @Override
    public String toString() {
        return "LostSearchCriteria [keyword=" + keyword + ", gender=" + gender + ", sterilization=" + sterilization
                + ", microChipNumber=" + microChipNumber + ", caseStateId=" + caseStateId + ", cityId=" + cityId
                + ", districtAreaId=" + districtAreaId + ", speciesId=" + speciesId + ", breedId=" + breedId
                + ", furColorId=" + furColorId + "]";
    }

}
