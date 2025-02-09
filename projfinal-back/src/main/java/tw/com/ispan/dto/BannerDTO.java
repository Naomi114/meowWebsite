package tw.com.ispan.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BannerDTO {
    private Integer bannerId;
    private String bannerType; // ✅ 修改成 String
    private LocalDateTime onlineDate;
    private String caseTitle;
    private String pictureUrl; // ✅ 這裡存案件圖片
}
