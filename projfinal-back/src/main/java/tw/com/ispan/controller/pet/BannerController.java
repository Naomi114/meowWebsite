package tw.com.ispan.controller.pet;

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
@RequestMapping("/lostBanners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 根據 Case ID 和 Banner 類型查詢 Banner
     */
    @GetMapping("/{caseId}/{bannerType}")
    public ResponseEntity<Banner> getBannerByCaseId(
            @PathVariable Integer caseId,
            @PathVariable BannerType bannerType) {

        Optional<Banner> banner = bannerService.getBannerByCaseId(caseId, bannerType);
        return banner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
