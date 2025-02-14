package tw.com.ispan.controller.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.banner.Banner;
import tw.com.ispan.domain.pet.banner.BannerType;
import tw.com.ispan.dto.pet.BannerDTO;
import tw.com.ispan.service.banner.BannerService;

@RestController
@CrossOrigin
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    // ✅ 透過 bannerId 查詢對應案件
    @GetMapping("/{bannerId}")
    public ResponseEntity<?> getCaseByBannerId(@PathVariable Integer bannerId) {
        Optional<Banner> bannerOpt = bannerService.findCaseByBannerId(bannerId);

        if (bannerOpt.isPresent()) {
            Banner banner = bannerOpt.get();

            // 🔍 根據不同的案件類型，回傳對應的案件資料
            if (banner.getLostCase() != null) {
                return ResponseEntity.ok(banner.getLostCase());
            } else if (banner.getRescueCase() != null) {
                return ResponseEntity.ok(banner.getRescueCase());
            } else if (banner.getAdoptionCase() != null) {
                return ResponseEntity.ok(banner.getAdoptionCase());
            } else {
                return ResponseEntity.badRequest().body("⚠️ 該 Banner 沒有對應的案件");
            }
        }
        return ResponseEntity.notFound().build();
    }

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
    public ResponseEntity<List<BannerDTO>> getAllBanners() {
        List<BannerDTO> banners = bannerService.getAllBanners();
        return ResponseEntity.ok(banners);
    }
}
