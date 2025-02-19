package tw.com.ispan.dto.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BannerDTO {
    private Integer bannerId;
    private String bannerType;
    private LocalDateTime onlineDate;
    private Integer lostCaseId;
    private Integer rescueCaseId;
    private Integer adoptionCaseId;
    private String caseTitle;
    private List<Map<String, String>> casePictures;

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public LocalDateTime getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(LocalDateTime onlineDate) {
        this.onlineDate = onlineDate;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public Integer getLostCaseId() {
        return lostCaseId;
    }

    public void setLostCaseId(Integer lostCaseId) {
        this.lostCaseId = lostCaseId;
    }

    public Integer getRescueCaseId() {
        return rescueCaseId;
    }

    public void setRescueCaseId(Integer rescueCaseId) {
        this.rescueCaseId = rescueCaseId;
    }

    public Integer getAdoptionCaseId() {
        return adoptionCaseId;
    }

    public void setAdoptionCaseId(Integer adoptionCaseId) {
        this.adoptionCaseId = adoptionCaseId;
    }

    public List<Map<String, String>> getCasePictures() {
        return casePictures;
    }

    public void setCasePictures(List<Map<String, String>> casePictures) {
        this.casePictures = casePictures; // ✅ 直接存儲 `casePictures`，不做 URL 轉換
    }
}
