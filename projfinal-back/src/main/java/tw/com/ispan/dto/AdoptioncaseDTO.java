package tw.com.ispan.dto;

public class AdoptioncaseDTO {
    private String caseTitle;
    private String story;
    private String healthCondition;
    private String adoptedCondition;
    private Integer memberId;
    private Integer caseStateId;
    private Integer speciesId;
    private Integer breedId;
    private Integer cityId;
    private Integer districtAreaId;
    private Integer furColorId;
    private String note;
    
    public String getCaseTitle() {
        return caseTitle;
    }
    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }
    public String getStory() {
        return story;
    }
    public void setStory(String story) {
        this.story = story;
    }
    public String getHealthCondition() {
        return healthCondition;
    }
    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }
    public String getAdoptedCondition() {
        return adoptedCondition;
    }
    public void setAdoptedCondition(String adoptedCondition) {
        this.adoptedCondition = adoptedCondition;
    }
    public Integer getMemberId() {
        return memberId;
    }
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
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
    public Integer getFurColorId() {
        return furColorId;
    }
    public void setFurColorId(Integer furColorId) {
        this.furColorId = furColorId;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Integer getCaseStateId() {
        return caseStateId;
    }
    public void setCaseStateId(Integer caseStateId) {
        this.caseStateId = caseStateId;
    }
    
}

