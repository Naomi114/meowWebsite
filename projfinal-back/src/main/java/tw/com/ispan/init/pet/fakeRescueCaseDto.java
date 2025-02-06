package tw.com.ispan.init.pet;

import java.util.List;
import java.util.stream.Collectors;

public class fakeRescueCaseDto {
    private Integer memberId;
    private String caseTitle;
    private Integer breedId;
    private Integer speciesId;
    private Integer furColorId;
    private Integer cityId;
    private Integer districtAreaId;
    private Integer caseStateId;
    private String gender;
    private String sterilization;
    private List<CasePictureDto> casePictures;
    private Integer age;
    private Integer microChipNumber;
    private List<RescueDemandDto> rescueDemands;
    private List<CanAffordDto> canAffords;
    private Boolean suspLost;
    private String street;
    private Double latitude;
    private Double longitude;
    private Integer donationAmount;
    private Integer viewCount;
    private Integer follow;
    private String tag;
    private String rescueReason;
    private String caseUrl;
    private Boolean isHidden;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public Integer getBreedId() {
        return breedId;
    }

    public void setBreedId(Integer breedId) {
        this.breedId = breedId;
    }

    public Integer getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }

    public Integer getFurColorId() {
        return furColorId;
    }

    public void setFurColorId(Integer furColorId) {
        this.furColorId = furColorId;
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

    public Integer getCaseStateId() {
        return caseStateId;
    }

    public void setCaseStateId(Integer caseStateId) {
        this.caseStateId = caseStateId;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(Integer donationAmount) {
        this.donationAmount = donationAmount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRescueReason() {
        return rescueReason;
    }

    public void setRescueReason(String rescueReason) {
        this.rescueReason = rescueReason;
    }

    public String getCaseUrl() {
        return caseUrl;
    }

    public void setCaseUrl(String caseUrl) {
        this.caseUrl = caseUrl;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public List<RescueDemandDto> getRescueDemands() {
        return rescueDemands;
    }

    public void setRescueDemands(List<RescueDemandDto> rescueDemands) {
        this.rescueDemands = rescueDemands;
    }

    public List<CanAffordDto> getCanAffords() {
        return canAffords;
    }

    public void setCanAffords(List<CanAffordDto> canAffords) {
        this.canAffords = canAffords;
    }

    public List<CasePictureDto> getCasePictures() {
        return casePictures;
    }

    public void setCasePictures(List<CasePictureDto> casePictures) {
        this.casePictures = casePictures;
    }

    // 轉換 casePictures 為 List<String>
    public List<String> getCasePictureUrls() {
        return casePictures.stream()
                .map(CasePictureDto::getUrl)
                .collect(Collectors.toList());
    }

}
