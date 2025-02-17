package tw.com.ispan.dto.pet;

import java.time.LocalDateTime;

public class BannerDTO {
    private Integer bannerId;
    private String bannerType;
    private LocalDateTime onlineDate;
    private Integer lostCaseId;
    private Integer rescueCaseId;
    private Integer adoptionCaseId;
    private String caseTitle;
    private String pictureUrl;
    private Integer pictureId; // ✅ 確保能返回圖片 ID

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
}
