package tw.com.ispan.dto.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tw.com.ispan.domain.pet.RescueCase;


public class OutputRescueCaseDTO {

	private Integer rescueCaseId;
	private String caseTitle;
	private String caseUrl;
	private Integer age;
	private String gender;
	private String sterilization;
	private Integer microChipNumber;
	private String rescueReason;
	private boolean suspLost;
	private boolean isHidden;
	private List<Map<String, String>> casePictures;

//	private Integer cityId;
	private String cityName;

//	private Integer districtAreaId;
	private String districtAreaName;

	private String street;
	private Double latitude;
	private Double longitude;

	private Integer donationAmount;
	private Integer viewCount;
	private Integer follow;
	private String tag;

	private String caseState;
	private String species;
	private String breed;
	private String furColor;

	private String memberNickName;
	private Integer memberId;      //用於讓用戶想要進入頁面可以編輯時，去對應前端用戶token以及案件回傳的memberId
	private LocalDateTime publicationTime;
	private LocalDateTime lastUpdateTime;

	private List<String> rescueDemands;
	private List<String> canAffords;
	
	// DTO 建構子：將 RescueCase 轉換為 OutputRescueCaseDTO
    public OutputRescueCaseDTO(RescueCase rescueCase) {
        this.rescueCaseId = rescueCase.getRescueCaseId();
        this.caseTitle = rescueCase.getCaseTitle();
        this.caseUrl = rescueCase.getCaseUrl();
        this.age = rescueCase.getAge();
        this.gender = rescueCase.getGender();
        this.sterilization = rescueCase.getSterilization();
        this.microChipNumber = rescueCase.getMicroChipNumber();
        this.rescueReason = rescueCase.getRescueReason();
        this.suspLost = rescueCase.getSuspLost();
        this.isHidden = rescueCase.getIsHidden();

        if (rescueCase.getCity() != null) {
//            this.cityId = rescueCase.getCity().getCityId();
            this.cityName = rescueCase.getCity().getCity();
        }

        if (rescueCase.getDistrictArea() != null) {
//            this.districtAreaId = rescueCase.getDistrictArea().getDistrictAreaId();
            this.districtAreaName = rescueCase.getDistrictArea().getDistrictAreaName();
        }

        this.street = rescueCase.getStreet();
        this.latitude = rescueCase.getLatitude();
        this.longitude = rescueCase.getLongitude();

        this.donationAmount = rescueCase.getDonationAmount();
        this.viewCount = rescueCase.getViewCount();
        this.follow = rescueCase.getFollow();
        this.tag = rescueCase.getTag();

        if (rescueCase.getCaseState() != null) {
            this.caseState = rescueCase.getCaseState().getCaseStatement();
        }

        if (rescueCase.getSpecies() != null) {
            this.species = rescueCase.getSpecies().getSpecies();
        }

        if (rescueCase.getBreed() != null) {
            this.breed = rescueCase.getBreed().getBreed();
        }

        if (rescueCase.getFurColor() != null) {
            this.furColor = rescueCase.getFurColor().getFurColor();
        }

        if (rescueCase.getMember() != null) {
            this.memberNickName = rescueCase.getMember().getNickName();
        }

        this.publicationTime = rescueCase.getPublicationTime();
        this.lastUpdateTime = rescueCase.getLastUpdateTime();

        this.rescueDemands = rescueCase.getRescueDemands().stream()
                .map(demand -> demand.getRescueDemand())
                .collect(Collectors.toList());

        this.canAffords = rescueCase.getCanAffords().stream()
                .map(afford -> afford.getCanAfford())
                .collect(Collectors.toList());
    }

	public Integer getRescueCaseId() {
		return rescueCaseId;
	}

	public void setRescueCaseId(Integer rescueCaseId) {
		this.rescueCaseId = rescueCaseId;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public String getCaseUrl() {
		return caseUrl;
	}

	public void setCaseUrl(String caseUrl) {
		this.caseUrl = caseUrl;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public Integer getMicroChipNumber() {
		return microChipNumber;
	}

	public void setMicroChipNumber(Integer microChipNumber) {
		this.microChipNumber = microChipNumber;
	}

	public String getRescueReason() {
		return rescueReason;
	}

	public void setRescueReason(String rescueReason) {
		this.rescueReason = rescueReason;
	}

	public boolean isSuspLost() {
		return suspLost;
	}

	public void setSuspLost(boolean suspLost) {
		this.suspLost = suspLost;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictAreaName() {
		return districtAreaName;
	}

	public void setDistrictAreaName(String districtAreaName) {
		this.districtAreaName = districtAreaName;
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

	public String getCaseState() {
		return caseState;
	}

	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getFurColor() {
		return furColor;
	}

	public void setFurColor(String furColor) {
		this.furColor = furColor;
	}

	public String getMemberNickName() {
		return memberNickName;
	}

	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}

	public LocalDateTime getPublicationTime() {
		return publicationTime;
	}

	public void setPublicationTime(LocalDateTime publicationTime) {
		this.publicationTime = publicationTime;
	}

	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public List<String> getRescueDemands() {
		return rescueDemands;
	}

	public void setRescueDemands(List<String> rescueDemands) {
		this.rescueDemands = rescueDemands;
	}

	public List<String> getCanAffords() {
		return canAffords;
	}

	public void setCanAffords(List<String> canAffords) {
		this.canAffords = canAffords;
	}

	public List<Map<String, String>> getCasePictures() {
		return casePictures;
	}

	public void setCasePictures(List<Map<String, String>> pictureUrls) {
		this.casePictures = pictureUrls;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
    
	
    
	
}
