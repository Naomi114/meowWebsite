package tw.com.ispan.controller.pet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.Banner.Banner;
import tw.com.ispan.domain.pet.Banner.BannerType;
import tw.com.ispan.service.banner.BannerService;

@RestController
@CrossOrigin
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 根據 Banner 類型查詢對應的 Banners
     *
     * @param bannerType Banner 類型（LOST, ADOPTION, RESCUE）
     * @return 符合條件的 Banner 列表
     */
    @PostMapping("/{bannerType}")
    public ResponseEntity<List<Banner>> getBannersByType(@PathVariable String bannerType) {
        try {
            BannerType type = BannerType.valueOf(bannerType.toUpperCase()); // 確保 ENUM 值正確
            List<Banner> banners = bannerService.findByBannerType(type);
            return ResponseEntity.ok(banners);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 參數錯誤時回傳 400 Bad Request
        }
    }

    /**
     * ✅ 返回所有的 Banner，並按照 `onlineDate` 降冪排序
     */
    @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners() {
        List<Banner> banners = bannerService.getAllBanners();

        // 過濾掉 `isHidden: true` 的 Banner
        banners = banners.stream()
                .filter(b -> !b.getIsHidden())
                .sorted((b1, b2) -> b2.getOnlineDate().compareTo(b1.getOnlineDate())) // 按時間排序
                .toList();

        return ResponseEntity.ok(banners);
    }
}
