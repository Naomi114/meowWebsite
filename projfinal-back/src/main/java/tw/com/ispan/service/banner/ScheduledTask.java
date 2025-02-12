package tw.com.ispan.service.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledTask {
    @Autowired
    private BannerService bannerService;

    // 設定每天午夜 (00:00) 自動執行
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleHideExpiredBanners() {
        bannerService.hideExpiredBanners();
        System.out.println("隱藏過期的 Banner 任務執行完畢");
    }
}
