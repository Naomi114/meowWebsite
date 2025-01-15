package tw.com.ispan.domain.pet;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	
	//必填(但為了測試先改成非必填)
	// 關聯到member表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "memberId", nullable = true, foreignKey = @ForeignKey(name = "FK_RescueCase_Member"))
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
	@JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_City"))
	private City city;

	//必填
	// 關聯到distinctArea表，雙向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "distinctAreaId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_DistinctArea"))
	private DistinctArea distinctArea;

	@Column(columnDefinition = "NVARCHAR(10)", name = "street")
	private String street;
	
	//必填(請求成功後記得改回來)
	// 10位數，8位小數
	@Column(name = "latitude", precision = 10, nullable = true)
	private Double latitude;
	
	//必填
	// 11位數，8位小數
	@Column(name = "longitude", precision = 11,  nullable = true)
	private Double longitude;

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
	
	@Column(name = "tag", nullable = true, columnDefinition = "nvarchar(100)")
	private String tag;
	
	//必填
	// 關聯到CaseState表，單向多對一
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "CaseStateId", nullable = false, foreignKey = @ForeignKey(name = "FK_RescueCase_CaseState"))
	private CaseState caseState;
	
	//必填
	@Column(name = "rescueReason", columnDefinition = "nvarchar(max)", nullable = false)
	private String rescueReason;

	@Column(name = "caseUrl", length = 255)
	private String caseUrl;
    
	//必填
	//關聯到CasePicture表，單向一對多，rescueCaseId外鍵會在CasePicture表中
	@OneToMany
	@JoinColumn(name = "rescueCaseId", foreignKey = @ForeignKey(name = "FK_CasePicture_RescueCase"))
	private List<CasePicture> casePictures;
	
	//必填
	//和rescueDemand單向多對多
    @ManyToMany
    @JoinTable(
        name = "RescueCase_RescueDemand",
        joinColumns = @JoinColumn(name = "rescueCaseId"),
        inverseJoinColumns = @JoinColumn(name = "rescueDemandId")
    )
    private List<RescueDemand> rescueDemands;
	
    //必填
    //和canAfford表為單向多對多(case找去afford)
    @ManyToMany
    @JoinTable(
        name = "CanAfford_RescueCase",
        joinColumns = @JoinColumn(name = "rescueCaseId"),
        inverseJoinColumns = @JoinColumn(name = "canAffordId")
    )
    private List<CanAfford> canAffords;
    
    
    //和RescueProgress表單向一對多(case找去RescueProgress)
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "rescueCaseId")
    private List<RescueProgress> rescueProgresses;
	
    
    @OneToMany(mappedBy = "rescueCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follows;
    
    
    // 關聯到ReportCase表，單向一對多
    @OneToMany(mappedBy = "rescueCase", cascade = CascadeType.ALL)
    private List<ReportCase> reportCases; 
    
	
    
    
    // Hibernate 進行實體的初始化需要用到空參建構子
	public RescueCase() {
		super();
	}



	//設定初始值(publicationTime、lastUpdateTime、caseState為待救援id=3)，在物件永續化存入之前會觸發
	@PrePersist
	public void prePersist() {
	    this.publicationTime = LocalDateTime.now();
	    this.lastUpdateTime = LocalDateTime.now();
//	    		Optional<CaseState> result = caseStateRepository.findById(3);
//	    		if (result != null && result.isPresent()) {
//	    			rescueCase.setCaseState(result.get());
//	    		}
	}
	
	//實體更新操作(save,merge)前會觸發，更改更新時間
	@PreUpdate
	public void preUpdate() {
	    this.lastUpdateTime = LocalDateTime.now();
	}

	
	
	//getter setter
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


	public City getCity() {
		return city;
	}


	public void setCity(City city) {
		this.city = city;
	}


	public DistinctArea getDistinctArea() {
		return distinctArea;
	}


	public void setDistinctArea(DistinctArea distinctArea) {
		this.distinctArea = distinctArea;
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


	public CaseState getCaseState() {
		return caseState;
	}


	public void setCaseState(CaseState caseState) {
		this.caseState = caseState;
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


	public List<RescueDemand> getRescueDemands() {
		return rescueDemands;
	}


	public void setRescueDemands(List<RescueDemand> rescueDemands) {
		this.rescueDemands = rescueDemands;
	}


	public List<CanAfford> getCanAffords() {
		return canAffords;
	}


	public void setCanAffords(List<CanAfford> canAffords) {
		this.canAffords = canAffords;
	}


	public List<RescueProgress> getRescueProgresses() {
		return rescueProgresses;
	}


	public void setRescueProgresses(List<RescueProgress> rescueProgresses) {
		this.rescueProgresses = rescueProgresses;
	}


	public List<Follow> getFollows() {
		return follows;
	}


	public void setFollows(List<Follow> follows) {
		this.follows = follows;
	}

	
	
	public List<ReportCase> getReportCases() {
		return reportCases;
	}


	public void setReportCases(List<ReportCase> reportCases) {
		this.reportCases = reportCases;
	}



	@Override
	public String toString() {
		return "RescueCase [rescueCaseId=" + rescueCaseId + ", caseTitle=" + caseTitle + ", member=" + member
				+ ", species=" + species + ", breed=" + breed + ", furColor=" + furColor + ", gender=" + gender
				+ ", sterilization=" + sterilization + ", age=" + age + ", microChipNumber=" + microChipNumber
				+ ", suspLost=" + suspLost + ", city=" + city + ", distinctArea=" + distinctArea + ", street=" + street
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", donationAmount=" + donationAmount
				+ ", viewCount=" + viewCount + ", follow=" + follow + ", publicationTime=" + publicationTime
				+ ", lastUpdateTime=" + lastUpdateTime + ", caseState=" + caseState + ", rescueReason="
				+ rescueReason + ", caseUrl=" + caseUrl + ", casePictures=" + casePictures + ", rescueDemands="
				+ rescueDemands + ", canAffords=" + canAffords + ", rescueProgresses=" + rescueProgresses + ", follows="
				+ follows + "]";
	}



	
}
