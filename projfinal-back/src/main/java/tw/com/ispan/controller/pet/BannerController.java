package tw.com.ispan.controller.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 根據 Case ID 和 Banner 類型查詢 Banner
     */
    @GetMapping("/{bannerType}")
    public ResponseEntity<Banner> getBannerByCaseId(
            @PathVariable Integer caseId,
            @PathVariable BannerType bannerType) {

        Optional<Banner> banner = bannerService.getBannerByCaseId(caseId, bannerType);
        return banner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
