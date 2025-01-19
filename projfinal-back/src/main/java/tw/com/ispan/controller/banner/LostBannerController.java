package tw.com.ispan.controller.banner;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.banner.LostBanner;
import tw.com.ispan.service.banner.LostBannerService;

@RestController
@RequestMapping("/lostBanners")
public class LostBannerController {

    @Autowired
    private LostBannerService lostBannerService;

    /**
     * 為 LostCase 創建廣告牆
     */
    @PostMapping
    public ResponseEntity<LostBanner> createBanner(@RequestBody Map<String, Object> request) {
        Integer lostCaseId = (Integer) request.get("lostCaseId");
        LocalDateTime onlineDate = LocalDateTime.parse((String) request.get("onlineDate"));
        LocalDateTime dueDate = LocalDateTime.parse((String) request.get("dueDate"));

        LostBanner createdBanner = lostBannerService.createBannerForLostCase(lostCaseId, onlineDate, dueDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
    }

    /**
     * 根據 LostCase ID 獲取廣告牆
     */
    @GetMapping("/{lostCaseId}")
    public ResponseEntity<LostBanner> getBannerByLostCaseId(@PathVariable Integer lostCaseId) {
        Optional<LostBanner> banner = lostBannerService.getBannerByLostCaseId(lostCaseId);
        return banner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 更新廣告牆日期
     */
    @PutMapping("/{bannerId}")
    public ResponseEntity<LostBanner> updateBannerDates(@PathVariable Integer bannerId,
            @RequestBody Map<String, String> request) {
        LocalDateTime newOnlineDate = LocalDateTime.parse(request.get("onlineDate"));
        LocalDateTime newDueDate = LocalDateTime.parse(request.get("dueDate"));

        LostBanner updatedBanner = lostBannerService.updateBannerDates(bannerId, newOnlineDate, newDueDate);
        return ResponseEntity.ok(updatedBanner);
    }

    /**
     * 刪除廣告牆
     */
    @DeleteMapping("/{bannerId}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Integer bannerId) {
        lostBannerService.deleteBanner(bannerId);
        return ResponseEntity.noContent().build();
    }
}
