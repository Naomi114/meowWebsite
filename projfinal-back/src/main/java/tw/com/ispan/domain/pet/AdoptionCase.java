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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.forAdopt.AdoptionCaseApply;

@Entity
@Table(name = "AdoptionCase")
public class AdoptionCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adoptionCaseId;

    @Column(name = "caseTitle", columnDefinition = "nvarchar(30)", nullable = false)
    private String caseTitle;

    // 雙向多對一,外鍵,對應member表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_Member"))
    private Member member;
    // 雙向多對一,外鍵,對應species表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "speciesId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_Species"))
    private Species species;
    // 雙向多對一,外鍵,對應breed表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "breedId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_Breed"))
    private Breed breed;
    // 雙向多對一,外鍵,對應furColor表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "furColorId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_FurColor"))
    private FurColor furColor;

    @Column(name = "gender", columnDefinition = "nvarchar(10)")
    private String gender;

    @Column(name = "sterilization", columnDefinition = "nvarchar(5)")
    private String sterilization;

    @Column(name = "age")
    private Integer age;

    @Column(name = "microChipNumber")
    private Integer microChipNumber;

    @Column(name = "susLost", nullable = false)
    private Boolean susLost;

    // 雙向多對一,外鍵,對應city表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "cityId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_City"))
    private City city;

    // 雙向多對一,外鍵,對應city表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "districtAreaId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_DistrictArea"))
    private DistrictArea districtArea;

    @Column(name = "street", columnDefinition = "NVARCHAR(10)")
    private String street;

    // 10位數，8位小數
    @Column(name = "latitude", precision = 10, scale = 8, nullable = true)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8, nullable = true)
    private BigDecimal longitude;

    @Column(name = "viewCount")
    private Integer viewCount;

    @Column(name = "follow")
    private Integer follow;

    @Column(name = "publicationTime", nullable = false)
    private LocalDateTime publicationTime;

    @Column(name = "lastUpdateTime", nullable = false)
    private LocalDateTime lastUpdateTime;

    @Column(name = "story", columnDefinition = "NVARCHAR(max)", nullable = false)
    private String story;

    @Column(name = "healthCondition", columnDefinition = "NVARCHAR(max)", nullable = false)
    private String healthCondition;

    @Column(name = "adoptedCondition", columnDefinition = "NVARCHAR(max)", nullable = false)
    private String adoptedCondition;

    @Column(name = "note", columnDefinition = "NVARCHAR(max)")
    private String note;

    // 單向多對一,外鍵,對應CaseState表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "caseStateId", nullable = false, foreignKey = @ForeignKey(name = "FK__AdoptionCaseState"))
    private CaseState caseState;

    // 關聯到CasePicture表，單向一對多，外鍵在一方
    @OneToMany
    @JoinColumn(name = "adoptionCaseId", foreignKey = @ForeignKey(name = "FK_CasePicture_AdoptionCase"))
    private List<CasePicture> casePictures;

    // 雙向一對多，對應follow表
    @OneToMany(mappedBy = "adoptionCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> follows;

    // 關聯到ReportCase表，單向一對多
    // 無外鍵，怕爛掉，測試版
    @OneToMany(mappedBy = "adoptionCase", cascade = CascadeType.ALL)
    private List<ReportCase> reportCase;

    // 與AdoptionCaseApply 多對多
    @ManyToMany
    @JoinTable(name = "Case_CaseApply", joinColumns = @JoinColumn(name = "adoptionCaseId", foreignKey = @ForeignKey(name = "FK_Case")), inverseJoinColumns = @JoinColumn(name = "adoptionCaseApplyId", foreignKey = @ForeignKey(name = "FK_CaseApply")))
    private Set<AdoptionCaseApply> adoptionCaseApply = new HashSet<>();

    public AdoptionCase() {

    }

    public AdoptionCase(Integer adoptionCaseId, String caseTitle, Member member, Species species, Breed breed,
            FurColor furColor, String gender, String sterilization, Integer age, Integer microChipNumber,
            Boolean susLost, City city, DistrictArea districtArea, String street, BigDecimal latitude,
            BigDecimal longitude, Integer viewCount, Integer follow, LocalDateTime publicationTime,
            LocalDateTime lastUpdateTime, String story, String healthCondition, String adoptedCondition, Integer status, String note,
            List<CasePicture> casePictures, Set<Follow> follows, List<ReportCase> reportCase,
            Set<AdoptionCaseApply> adoptionCaseApply, CaseState caseState) {
        this.adoptionCaseId = adoptionCaseId;
        this.caseTitle = caseTitle;
        this.member = member;
        this.species = species;
        this.breed = breed;
        this.furColor = furColor;
        this.gender = gender;
        this.sterilization = sterilization;
        this.age = age;
        this.microChipNumber = microChipNumber;
        this.susLost = susLost;
        this.city = city;
        this.districtArea = districtArea;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
        this.viewCount = viewCount;
        this.follow = follow;
        this.publicationTime = publicationTime;
        this.lastUpdateTime = lastUpdateTime;
        this.story = story;
        this.healthCondition = healthCondition;
        this.adoptedCondition = adoptedCondition;

        this.note = note;
        this.casePictures = casePictures;
        this.follows = follows;
        this.reportCase = reportCase;
        this.adoptionCaseApply = adoptionCaseApply;
        this.caseState = caseState; // 確保在構造時就初始化 caseState
    }

    @Override
    public String toString() {
        return "AdoptionCase [adoptionCaseId=" + adoptionCaseId + ", caseTitle=" + caseTitle + ", member=" + member
                + ", species=" + species + ", breed=" + breed + ", furColor=" + furColor + ", gender=" + gender
                + ", sterilization=" + sterilization + ", age=" + age + ", microChipNumber=" + microChipNumber
                + ", susLost=" + susLost + ", city=" + city + ", districtArea=" + districtArea + ", street=" + street
                + ", latitude=" + latitude + ", longitude=" + longitude + ", viewCount=" + viewCount + ", follow="
                + follow + ", publicationTime=" + publicationTime + ", lastUpdateTime=" + lastUpdateTime  + ", story=" + story + ", healthCondition=" + healthCondition + ", adoptedCondition="
                + adoptedCondition + ", note=" + note + ", caseState=" + caseState + ", casePictures=" + casePictures
                + ", follows=" + follows + ", reportCase=" + reportCase + ", adoptionCaseApply=" + adoptionCaseApply
                + "]";
    }

    public Integer getAdoptionCaseId() {
        return adoptionCaseId;
    }

    public void setAdoptionCaseId(Integer adoptionCaseId) {
        this.adoptionCaseId = adoptionCaseId;
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

    public Boolean getSusLost() {
        return susLost;
    }

    public void setSusLost(Boolean susLost) {
        this.susLost = susLost;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CaseState getCaseState() {
        return caseState;
    }

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
    }

    public List<CasePicture> getCasePictures() {
        return casePictures;
    }

    public void setCasePictures(List<CasePicture> casePictures) {
        this.casePictures = casePictures;
    }

    public Set<Follow> getFollows() {
        return follows;
    }

    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }

    public List<ReportCase> getReportCase() {
        return reportCase;
    }

    public void setReportCase(List<ReportCase> reportCase) {
        this.reportCase = reportCase;
    }

    public Set<AdoptionCaseApply> getAdoptionCaseApply() {
        return adoptionCaseApply;
    }

    public void setAdoptionCaseApply(Set<AdoptionCaseApply> adoptionCaseApply) {
        this.adoptionCaseApply = adoptionCaseApply;
    }

    

}
