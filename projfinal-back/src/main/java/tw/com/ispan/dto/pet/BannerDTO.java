package tw.com.ispan.dto.pet;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BannerDTO {
    private Integer bannerId;
    private String bannerType;
    private LocalDateTime onlineDate;
    private String caseTitle;
    private String pictureUrl;
    private Integer pictureId; // ✅ 確保能返回圖片 ID

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
}
