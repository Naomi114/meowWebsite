package tw.com.ispan.domain.pet;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.banner.Banner;

@Entity
@Table(name = "LostCase", indexes = {
        @Index(name = "idx_species", columnList = "speciesId"),
        @Index(name = "idx_breed", columnList = "breedId"),
        @Index(name = "idx_furColor", columnList = "furColorId"),
        @Index(name = "idx_city", columnList = "cityId"),
        @Index(name = "idx_districtArea", columnList = "districtAreaId")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lostCaseId")
// 使用lostCaseId作為唯一標識符
public class LostCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lostCaseId;

    @Column(columnDefinition = "NVARCHAR(30)", name = "caseTitle", nullable = false)
    private String caseTitle;

    // 關聯到 Member 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "memberId", nullable = true, foreignKey = @ForeignKey(name = "FK_LostCase_Member"))
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

    @Column(columnDefinition = "NVARCHAR(5)", name = "petName")
    private String name;

    @Column(columnDefinition = "NVARCHAR(5)", name = "gender")
    private String gender;

    @Column(columnDefinition = "NVARCHAR(5)", name = "sterilization", nullable = false)
    private String sterilization;

    @Column(name = "age")
    private Integer age;

    @Column(name = "microChipNumber")
    private Integer microChipNumber;

    // 關聯到 City 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_City"))
    private City city;

    // 關聯到 districtArea 表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "districtAreaId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_districtArea"))
    private DistrictArea districtArea;

    @Column(columnDefinition = "NVARCHAR(10)", name = "street")
    private String street;

    // 必填(請求成功後記得改回來)
    // 10位數，8位小數
    @Column(name = "latitude", precision = 10, nullable = true)
    private Double latitude;

    // 必填
    // 11位數，8位小數
    @Column(name = "longitude", precision = 11, nullable = true)
    private Double longitude;

    @Column(name = "donationAmount")
    private Integer donationAmount = 0;

    @Column(name = "viewCount")
    private Integer viewCount = 0;

    @Column(name = "follow") // 被追蹤數
    private Integer follow = 0;

    // 必填(非使用者手動填寫)
    @Column(name = "publicationTime", nullable = false)
    private LocalDateTime publicationTime;

    // 必填
    @Column(name = "lastUpdateTime", nullable = false)
    private LocalDateTime lastUpdateTime;

    @Lob
    @Column(name = "lostExperience", nullable = false)
    private String lostExperience;

    @Lob
    @Column(name = "contactInformation")
    private String contactInformation;

    @Lob
    @Column(name = "featureDescription", nullable = false)
    private String featureDescription;

    // 必填
    // 關聯到CasePicture表，單向一對多，註釋在這但rescueCaseId外鍵會在CasePicture表中
    // 當初設計一個case需放多張圖，由於想要減少表格數??? 把不同case的圖都放在同一張表，因此雖然設立3個case外鍵，但都須設為null
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lostCase", foreignKey = @ForeignKey(name = "FK_CasePicture_LostCase"))
    private List<CasePicture> casePictures;

    // 關聯到follow表(為會員和案件的追蹤中介表) 雙向一對多
    @OneToMany(mappedBy = "lostCase", cascade = CascadeType.PERSIST, orphanRemoval = true)
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

    // 設定初始值(publicationTime、lastUpdateTime、caseState為待救援id=5)，在物件永續化存入之前會觸發
    @PrePersist
    public void prePersist() {
        this.publicationTime = LocalDateTime.now();
        this.lastUpdateTime = LocalDateTime.now();
    }

    // 實體更新操作(save,merge)前會觸發，更改更新時間
    @PreUpdate
    public void preUpdate() {
        this.lastUpdateTime = LocalDateTime.now();
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public DistrictArea getDistrictArea() {
        return districtArea;
    }

    public void setDistrictArea(DistrictArea districtArea) {
        this.districtArea = districtArea;
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

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public List<Follow> getFollows() {
        return follows;
    }

    public void setFollows(List<Follow> follows) {
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
                ", city=" + city +
                ", districtArea=" + districtArea +
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
