package tw.com.ispan.dto.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tw.com.ispan.domain.pet.LostCase;

public class OutputLostCaseDTO {

	private Integer lostCaseId;
	private String caseTitle;
	private String name;
	private Integer age;
	private String gender;
	private String sterilization;
	private String microChipNumber;
	private boolean isHidden;
	private String pictureUrl;
	private Integer pictureId; // ✅ 確保能返回圖片 ID

	// private Integer cityId;
	private String cityName;

	// private Integer districtAreaId;
	private String districtAreaName;

	private String street;
	private Double latitude;
	private Double longitude;

	private Integer donationAmount;
	private Integer viewCount;
	private Integer follow;

	private String caseState;
	private String species;
	private String breed;
	private String furColor;

	private String memberNickName;
	private Integer memberId; // 用於讓用戶想要進入頁面可以編輯時，去對應前端用戶token以及案件回傳的memberId
	private LocalDateTime publicationTime;
	private LocalDateTime lastUpdateTime;

	private String contactInformation;
	private String featureDescription;
	private String lostExperience;

	// DTO 建構子：將 lostCase 轉換為 OutputlostCaseDTO
	public OutputLostCaseDTO(LostCase lostCase) {
		this.lostCaseId = lostCase.getLostCaseId();
		this.caseTitle = lostCase.getName();
		this.name = lostCase.getCaseTitle();
		this.age = lostCase.getAge();
		this.gender = lostCase.getGender();
		this.sterilization = lostCase.getSterilization();
		this.microChipNumber = lostCase.getMicroChipNumber();
		this.isHidden = lostCase.getIsHidden();

		if (lostCase.getCity() != null) {
			// this.cityId = lostCase.getCity().getCityId();
			this.cityName = lostCase.getCity().getCity();
		}

		if (lostCase.getDistrictArea() != null) {
			// this.districtAreaId = lostCase.getDistrictArea().getDistrictAreaId();
			this.districtAreaName = lostCase.getDistrictArea().getDistrictAreaName();
		}

		this.street = lostCase.getStreet();
		this.latitude = lostCase.getLatitude();
		this.longitude = lostCase.getLongitude();

		this.donationAmount = lostCase.getDonationAmount();
		this.viewCount = lostCase.getViewCount();
		this.follow = lostCase.getFollow();

		if (lostCase.getCaseState() != null) {
			this.caseState = lostCase.getCaseState().getCaseStatement();
		}

		if (lostCase.getSpecies() != null) {
			this.species = lostCase.getSpecies().getSpecies();
		}

		if (lostCase.getBreed() != null) {
			this.breed = lostCase.getBreed().getBreed();
		}

		if (lostCase.getFurColor() != null) {
			this.furColor = lostCase.getFurColor().getFurColor();
		}

		if (lostCase.getMember() != null) {
			this.memberNickName = lostCase.getMember().getNickName();
		}

		this.publicationTime = lostCase.getPublicationTime();
		this.lastUpdateTime = lostCase.getLastUpdateTime();

		this.contactInformation = lostCase.getContactInformation();
		this.featureDescription = lostCase.getFeatureDescription();
		this.lostExperience = lostCase.getLostExperience();
	}

	public Integer getLostCaseId() {
		return lostCaseId;
	}

	public void setLostCaseId(Integer lostCaseId) {
		this.lostCaseId = lostCaseId;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getMicroChipNumber() {
		return microChipNumber;
	}

	public void setMicroChipNumber(String microChipNumber) {
		this.microChipNumber = microChipNumber;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Integer getPictureId() {
		return pictureId;
	}

	public void setPictureId(Integer pictureId) {
		this.pictureId = pictureId;
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

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
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

	public String getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	public String getLostExperience() {
		return lostExperience;
	}

	public void setLostExperience(String lostExperience) {
		this.lostExperience = lostExperience;
	}

}
