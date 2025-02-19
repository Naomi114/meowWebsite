package tw.com.ispan.dto.pet;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BannerDTO {
    private Integer bannerId;
    private String bannerType;
    private LocalDateTime onlineDate;
    private Integer lostCaseId;
    private Integer rescueCaseId;
    private Integer adoptionCaseId;
    private String caseTitle;
    private List<Map<String, String>> casePictures;

    // ✅ 取得 API Base URL，從環境變數 `VITE_API_BASE_URL` 或預設為本機開發環境
    private static final String BASE_URL = System.getenv("VITE_API_BASE_URL") != null
            ? System.getenv("VITE_API_BASE_URL")
            : "http://localhost:8080";

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

    /**
     * ✅ 設置 `casePictures`，並轉換為雲端 URL
     */
    public void setCasePictures(List<Map<String, String>> casePictures) {
        if (casePictures == null || casePictures.isEmpty()) {
            // 若無圖片，則使用預設圖片
            Map<String, String> defaultImage = new HashMap<>();
            defaultImage.put("pictureUrl", BASE_URL + "/images/default.png");
            this.casePictures = Collections.singletonList(defaultImage);
        } else {
            // 轉換所有圖片的 URL
            this.casePictures = casePictures.stream()
                    .map(pic -> {
                        String filePath = pic.get("pictureUrl");
                        if (filePath == null || filePath.isEmpty()) {
                            filePath = BASE_URL + "/images/default.png";
                        } else {
                            filePath = convertBackendPath(filePath);
                        }

                        Map<String, String> updatedPic = new HashMap<>();
                        updatedPic.put("pictureUrl", filePath);
                        return updatedPic;
                    })
                    .collect(Collectors.toList());
        }
    }

    /**
     * ✅ 將後端的本機路徑轉換為前端可讀取的 URL
     */
    private String convertBackendPath(String path) {
        if (path == null || path.isEmpty()) {
            return BASE_URL + "/images/default.png";
        }

        path = path.replace("\\", "/"); // 確保 `/` 格式一致（適用於 Windows & Linux）

        if (path.startsWith("C:/upload/final/")) {
            // ✅ 轉換本機路徑為雲端 URL
            return path.replace("C:/upload/final", BASE_URL + "/upload/final");
        } else if (path.startsWith("/upload/final/")) {
            // ✅ 如果是相對路徑，補上 BASE_URL
            return BASE_URL + path;
        } else if (path.startsWith("http")) {
            // ✅ 若已經是完整 URL，則不修改
            return path;
        }
        return BASE_URL + "/images/default.png"; // 若發生錯誤，返回預設圖片
    }
}
