package tw.com.ispan.domain.pet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.Banner.Banner;

@Entity
@Table(name = "LostCase")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lostCaseId" // 使用 lostCaseId
                                                                                                  // 作為唯一標識符
)
public class LostCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lostCaseId")
    private Integer lostCaseId;

    @Column(columnDefinition = "NVARCHAR(30)", name = "caseTitle", nullable = false)
    private String caseTitle;

    // 關聯到 Member 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "memberId", nullable = true, foreignKey = @ForeignKey(name = "FK_LostCase_Member"))
    @JsonBackReference
    private Member member;

    // 關聯到 Species 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "speciesId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_Species"))
    private Species species;

    // 關聯到 Breed 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "breedId", foreignKey = @ForeignKey(name = "FK_LostCase_Breed"))
    private Breed breed;

    // 關聯到 FurColor 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "furColorId", foreignKey = @ForeignKey(name = "FK_LostCase_FurColor"))
    private FurColor furColor;

    @Column(columnDefinition = "NVARCHAR(5)", name = "name")
    private String name;

    @Column(columnDefinition = "NVARCHAR(5)", name = "gender")
    private String gender;

    @Column(columnDefinition = "NVARCHAR(5)", name = "sterilization", nullable = false)
    private String sterilization;

    @Column(name = "age")
    private Integer age;

    @Column(name = "microChipNumber")
    private Integer microChipNumber;

    @Column(name = "suspLost")
    private boolean suspLost;

    // 關聯到 City 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_City"))
    private City city;

    // 關聯到 DistinctArea 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "distinctAreaId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_distinctArea"))
    private DistinctArea distinctArea;

    @Column(columnDefinition = "NVARCHAR(10)", name = "street", nullable = false)
    private String street;

    @Column(name = "latitude", precision = 10, scale = 8, nullable = false)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8, nullable = false)
    private BigDecimal longitude;

    @Column(name = "donationAmount")
    private Integer donationAmount;

    @Column(name = "viewCount")
    private Integer viewCount;

    @Column(name = "publicationTime", nullable = true)
    private LocalDateTime publicationTime;

    @Column(name = "lastUpdateTime", nullable = true)
    private LocalDateTime lastUpdateTime;

    @Lob
    @Column(name = "lostExperience")
    private String lostExperience;

    @Lob
    @Column(name = "contactInformation")
    private String contactInformation;

    @Lob
    @Column(name = "featureDescription")
    private String featureDescription;

    // 關聯到 CasePicture 表，單向一對多
    @OneToMany
    @JoinColumn(name = "lostCase", foreignKey = @ForeignKey(name = "FK_CasePicture_LostCase"))
    private List<CasePicture> casePictures;

    @OneToMany(mappedBy = "lostCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follows;

    // 關聯到 ReportCase 表，單向一對多
    @OneToMany(mappedBy = "lostCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportCase> reportCases;

    // 必填，與 CaseState 單向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "caseStateId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_CaseState"))
    private CaseState caseState;

    @Column(name = "caseUrl", length = 255)
    private String caseUrl;

    @OneToOne(mappedBy = "lostCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Banner banner;

    @Column(name = "isHidden", nullable = false)
    private Boolean isHidden = false; // 默認為不隱藏

    // 空參數建構子 (Hibernate 要求)
    public LostCase() {
        super();
    }

    // 全參數建構子
    public LostCase(
            Integer lostCaseId, String caseTitle,
            Member member,
            List<ReportCase> reportCases,
            Species species, Breed breed, FurColor furColor,
            String name, String gender, String sterilization, Integer age, Integer microChipNumber, boolean suspLost,
            City city, DistinctArea distinctArea, String street, BigDecimal latitude, BigDecimal longitude,
            Integer donationAmount, Integer viewCount, List<Follow> follows, LocalDateTime publicationTime,
            LocalDateTime lastUpdateTime, String lostExperience, String contactInformation, String featureDescription,
            List<CasePicture> casePictures, CaseState caseState, String caseUrl, Banner banner,
            Boolean isHidden) {
        this.lostCaseId = lostCaseId;
        this.caseTitle = caseTitle;
        this.member = member;
        this.species = species;
        this.breed = breed;
        this.furColor = furColor;
        this.name = name;
        this.gender = gender;
        this.sterilization = sterilization;
        this.age = age;
        this.microChipNumber = microChipNumber;
        this.suspLost = suspLost;
        this.city = city;
        this.distinctArea = distinctArea;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
        this.donationAmount = donationAmount;
        this.viewCount = viewCount;
        this.follows = follows;
        this.publicationTime = publicationTime;
        this.lastUpdateTime = lastUpdateTime;
        this.lostExperience = lostExperience;
        this.contactInformation = contactInformation;
        this.featureDescription = featureDescription;
        this.casePictures = casePictures;
        this.reportCases = reportCases;
        this.caseState = caseState;
        this.caseUrl = caseUrl;
        this.banner = banner;
        this.isHidden = isHidden;
    }

    // Getter & Setter
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isSuspLost() {
        return suspLost;
    }

    public void setSuspLost(boolean suspLost) {
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

    public List<Follow> getFollow() {
        return follows;
    }

    public void setFollow(List<Follow> follows) {
        this.follows = follows;
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

    public String getLostExperience() {
        return lostExperience;
    }

    public void setLostExperience(String lostExperience) {
        this.lostExperience = lostExperience;
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

    public List<CasePicture> getCasePictures() {
        return casePictures;
    }

    public void setCasePictures(List<CasePicture> casePictures) {
        this.casePictures = casePictures;
    }

    public List<ReportCase> getReportCases() {
        return reportCases;
    }

    public void setReportCases(List<ReportCase> reportCases) {
        this.reportCases = reportCases;
    }

    public CaseState getCaseState() {
        return caseState;
    }

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
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

    public Banner getBanner() {
        return banner;
    }

    public void setBanners(Banner banner) {
        this.banner = banner;
    }

    @Override
    public String toString() {
        return "LostCase [lostCaseId=" + lostCaseId +
                ", caseTitle=" + caseTitle +
                ", member=" + member +
                ", species=" + species +
                ", breed=" + breed +
                ", furColor=" + furColor +
                ", name=" + name +
                ", gender=" + gender +
                ", sterilization=" + sterilization +
                ", age=" + age +
                ", microChipNumber=" + microChipNumber +
                ", suspLost=" + suspLost +
                ", city=" + city +
                ", distinctArea=" + distinctArea +
                ", street=" + street +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", donationAmount=" + donationAmount +
                ", viewCount=" + viewCount +
                ", publicationTime=" + publicationTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", lostExperience=" + lostExperience +
                ", contactInformation=" + contactInformation +
                ", featureDescription=" + featureDescription +
                ", casePictures=" + casePictures +
                ", follows=" + follows +
                ", reportCases=" + reportCases +
                ", caseState=" + caseState +
                ", caseUrl=" + caseUrl +
                ", banner=" + banner +
                ", isHidden=" + isHidden +
                "]";
    }

}
