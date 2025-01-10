package tw.com.ispan.domain.pet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.forRescue.CanAfford;
import tw.com.ispan.domain.pet.forRescue.RescueDemand;
import tw.com.ispan.domain.pet.forRescue.RescueProgress;

@Entity
@Table(name = "RescueCase")
public class RescueCase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rescueCaseId;
	
	//必填
	@Column(columnDefinition = "NVARCHAR(30)", name = "caseTitle", nullable = false)
	private String caseTitle;
	
	//必填
	// 關聯到member表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_Member"))
	private Member member;

	//必填
	// 關聯到species表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "speciesId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_Species"))
	private Species species;

	// 關聯到breed表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "breedId", foreignKey = @ForeignKey(name = "FK_RescueCase_Breed"))
	private Breed breed;

	// 關聯到furColor表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "furColorId", foreignKey = @ForeignKey(name = "FK_RescueCase_FurColor"))
	private FurColor furColor;

	@Column(columnDefinition = "NVARCHAR(5)", name = "gender")
	private String gender;

	@Column(columnDefinition = "NVARCHAR(5)", name = "sterilization")
	private String sterilization;

	@Column(name = "age")
	private Integer age;

	@Column(name = "microChipNumber")
	private Integer microChipNumber;
	
	//必填
	@Column(name = "suspLost", nullable = false)
	private Boolean suspLost;
	
	//必填
	// 關聯到city表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_FurColor"))
	private City cityId;

	//必填
	// 關聯到distinct表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "distinctId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_Distinct"))
	private Distinct distinctId;

	@Column(columnDefinition = "NVARCHAR(10)", name = "street")
	private String street;
	
	//必填
	// 10位數，8位小數
	@Column(name = "latitude", precision = 10, scale = 8, nullable = false)
	private BigDecimal latitude;
	
	//必填
	// 11位數，8位小數
	@Column(name = "longitude", precision = 11, scale = 8, nullable = false)
	private BigDecimal longitude;

	@Column(name = "donationAmount")
	private Integer donationAmount;

	@Column(name = "viewCount")
	private Integer viewCount;   

	@Column(name = "follow")
	private Integer follow;
	
	//必填
	@Column(name = "publicationTime", nullable = false)
	private LocalDateTime publicationTime;
	
	//必填
	@Column(name = "lastUpdateTime", nullable = false)
	private LocalDateTime lastUpdateTime;
	
	//必填
	// 關聯到CaseState表，單向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "CaseStateId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_CaseState"))
	private CaseState caseStateId;
	
	//必填
	@Column(name = "rescueReason", columnDefinition = "nvarchar(max)", nullable = false)
	private String rescueReason;

	@Column(name = "caseUrl", length = 255)
	private String caseUrl;
    
	//必填
	//關聯到CasePicture表，單向一對多，rescueCaseId外鍵會在CasePicture表中
	@OneToMany
	@JoinColumn(name = "rescueCaseId", foreignKey = @ForeignKey(name = "FK_CasePicture_RescueCase"), nullable = false)
	private List<CasePicture> casePictures;
	
	//必填
	//和rescueDemand單向多對多
    @ManyToMany
    @JoinTable(
        name = "RescueCase_RescueDemand",
        joinColumns = @JoinColumn(name = "rescueCaseId"),
        inverseJoinColumns = @JoinColumn(name = "rescueDemandId")
    )
    private Set<RescueDemand> rescueDemands = new HashSet<>();
	
    //必填
    //和canAfford表為單向多對多(case找去afford)
    @ManyToMany
    @JoinTable(
        name = "CanAfford_RescueCase",
        joinColumns = @JoinColumn(name = "rescueCaseId"),
        inverseJoinColumns = @JoinColumn(name = "canAffordId")
    )
    private Set<CanAfford> canAffords;
    
    
    //和RescueProgress表單向一對多(case找去RescueProgress)
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "rescueCaseId")
    private Set<RescueProgress> rescueProgresses;
	
    
    @OneToMany(mappedBy = "rescueCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> follows;
    
    
	// Hibernate 進行實體的初始化需要用到空參建構子
	public RescueCase() {
		super();
	}



	public RescueCase(Integer rescueCaseId, String caseTitle, Member member, Species species, Breed breed,
			FurColor furColor, String gender, String sterilization, Integer age, Integer microChipNumber,
			Boolean suspLost, City cityId, Distinct distinctId, String street, BigDecimal latitude, BigDecimal longitude,
			Integer donationAmount, Integer viewCount, Integer follow, LocalDateTime publicationTime,
			LocalDateTime lastUpdateTime, CaseState caseStateId, String rescueReason, String caseUrl,
			List<CasePicture> casePictures, Set<RescueDemand> rescueDemands, Set<CanAfford> canAffords,
			Set<RescueProgress> rescueProgresses, Set<Follow> follows) {
		super();
		this.rescueCaseId = rescueCaseId;
		this.caseTitle = caseTitle;
		this.member = member;
		this.species = species;
		this.breed = breed;
		this.furColor = furColor;
		this.gender = gender;
		this.sterilization = sterilization;
		this.age = age;
		this.microChipNumber = microChipNumber;
		this.suspLost = suspLost;
		this.cityId = cityId;
		this.distinctId = distinctId;
		this.street = street;
		this.latitude = latitude;
		this.longitude = longitude;
		this.donationAmount = donationAmount;
		this.viewCount = viewCount;
		this.follow = follow;
		this.publicationTime = publicationTime;
		this.lastUpdateTime = lastUpdateTime;
		this.caseStateId = caseStateId;
		this.rescueReason = rescueReason;
		this.caseUrl = caseUrl;
		this.casePictures = casePictures;
		this.rescueDemands = rescueDemands;
		this.canAffords = canAffords;
		this.rescueProgresses = rescueProgresses;
		this.follows = follows;
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


	public Member getMember() {
		return member;
	}


	public void setMember(Member member) {
		this.member = member;
	}


	public Species getSpecies() {
		return species;
	}


	public void setSpecies(Species species) {
		this.species = species;
	}


	public Breed getBreed() {
		return breed;
	}


	public void setBreed(Breed breed) {
		this.breed = breed;
	}


	public FurColor getFurColor() {
		return furColor;
	}


	public void setFurColor(FurColor furColor) {
		this.furColor = furColor;
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


	public City getCityId() {
		return cityId;
	}


	public void setCityId(City cityId) {
		this.cityId = cityId;
	}


	public Distinct getDistinctId() {
		return distinctId;
	}


	public void setDistinctId(Distinct distinctId) {
		this.distinctId = distinctId;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public BigDecimal getLatitude() {
		return latitude;
	}


	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}


	public BigDecimal getLongitude() {
		return longitude;
	}


	public void setLongitude(BigDecimal longitude) {
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


	public CaseState getCaseStateId() {
		return caseStateId;
	}


	public void setCaseStateId(CaseState caseStateId) {
		this.caseStateId = caseStateId;
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


	public List<CasePicture> getCasePictures() {
		return casePictures;
	}


	public void setCasePictures(List<CasePicture> casePictures) {
		this.casePictures = casePictures;
	}


	public Set<RescueDemand> getRescueDemands() {
		return rescueDemands;
	}


	public void setRescueDemands(Set<RescueDemand> rescueDemands) {
		this.rescueDemands = rescueDemands;
	}


	public Set<CanAfford> getCanAffords() {
		return canAffords;
	}


	public void setCanAffords(Set<CanAfford> canAffords) {
		this.canAffords = canAffords;
	}


	public Set<RescueProgress> getRescueProgresses() {
		return rescueProgresses;
	}


	public void setRescueProgresses(Set<RescueProgress> rescueProgresses) {
		this.rescueProgresses = rescueProgresses;
	}


	public Set<Follow> getFollows() {
		return follows;
	}


	public void setFollows(Set<Follow> follows) {
		this.follows = follows;
	}


	@Override
	public String toString() {
		return "RescueCase [rescueCaseId=" + rescueCaseId + ", caseTitle=" + caseTitle + ", member=" + member
				+ ", species=" + species + ", breed=" + breed + ", furColor=" + furColor + ", gender=" + gender
				+ ", sterilization=" + sterilization + ", age=" + age + ", microChipNumber=" + microChipNumber
				+ ", suspLost=" + suspLost + ", cityId=" + cityId + ", distinctId=" + distinctId + ", street=" + street
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", donationAmount=" + donationAmount
				+ ", viewCount=" + viewCount + ", follow=" + follow + ", publicationTime=" + publicationTime
				+ ", lastUpdateTime=" + lastUpdateTime + ", caseStateId=" + caseStateId + ", rescueReason="
				+ rescueReason + ", caseUrl=" + caseUrl + ", casePictures=" + casePictures + ", rescueDemands="
				+ rescueDemands + ", canAffords=" + canAffords + ", rescueProgresses=" + rescueProgresses + ", follows="
				+ follows + "]";
	}



	
}
