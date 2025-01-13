package tw.com.ispan.domain.pet;

import java.math.BigDecimal;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "LostCase")
public class LostCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
    @Column(name = "lostCaseId")
    private Integer lostCaseId;

    @Column(columnDefinition = "NVARCHAR(30)", name = "caseTitle", nullable = false)
    private String caseTitle;

    // 關聯到member表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_Member"))
    private Member member;

    // 關聯到species表，單向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "specieId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_Specie"))
    private Species species;

    // 關聯到breed表，單向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "breedId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_Breed"))
    private Breed breed;

    // 關聯到furColor表，單向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "furColorId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_FurColor"))
    private FurColor furColor;

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

    // 關聯到city表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_FurColor"))
    private City cityId;

    // 關聯到distint表，雙向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "distinctId", nullable = false, foreignKey = @ForeignKey(name = "FK_LostCase_Distint"))
    private Distinct distinctId;

    @Column(columnDefinition = "NVARCHAR(10)", name = "street", nullable = false)
    private String street;

    // 10位數，8位小數
    @Column(name = "latitude", precision = 10, scale = 8, nullable = false)
    private BigDecimal latitude;// 經度

    // 11位數，8位小數
    @Column(name = "longitude", precision = 11, scale = 8, nullable = false)
    private BigDecimal longitude;// 緯度

    @Column(name = "donationAmount")
    private Integer donationAmount;

    @Column(name = "viewCount")
    private Integer viewCount;

    @Column(name = "follow")
    private Integer follow;

    @Column(name = "publicationTime", nullable = false)
    private LocalDateTime publicationTime;

    @Column(name = "lastUpdateTime", nullable = false)
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

    // 關聯到CasePicture表，單向一對多，lostCaseId外鍵會在CasePicture表中
    @OneToMany
    @JoinColumn(name = "lostCaseId", foreignKey = @ForeignKey(name = "FK_CasePicture_LostCase"))
    private List<CasePicture> casePictures;

    // 關聯到ReportCase表，單向一對多，lostCaseId外鍵會在ReportCase表中
    @OneToMany(mappedBy = "lostCaseId", cascade = CascadeType.ALL)
    private List<ReportCase> reportCases; // 一對多關聯

    @Override
    public String toString() {
        return "LostCase [lostCaseId=" + lostCaseId + ", caseTitle=" + caseTitle + ", member=" + member
                + ", species=" + species + ", breed=" + breed + ", furColor=" + furColor + ", gender=" + gender
                + ", sterilization=" + sterilization + ", age=" + age + ", microChipNumber=" + microChipNumber
                + ", suspLost=" + suspLost + ", cityId=" + cityId + ", distinctId=" + distinctId + ", street=" + street
                + ", latitude=" + latitude + ", longitude=" + longitude + ", donationAmount=" + donationAmount
                + ", viewCount=" + viewCount + ", follow=" + follow + ", publicationTime=" + publicationTime
                + ", lastUpdateTime=" + lastUpdateTime + ", lostExperience=" + lostExperience + ", contactInformation="
                + contactInformation + ", featureDescription=" + featureDescription + ", casePictures=" + casePictures
                + "]";
    }

    // Getters and Setters
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

    public void setDistintcId(Distinct distinctId) {
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

    public List<CasePicture> getCasePictures() {
        return casePictures;
    }

    public void setCasePictures(List<CasePicture> casePictures) {
        this.casePictures = casePictures;
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

    public List<ReportCase> getReportCases() {
        return reportCases;
    }

    public void setReportCases(List<ReportCase> reportCases) {
        this.reportCases = reportCases;
    }
}
