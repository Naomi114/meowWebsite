package tw.com.ispan.controller.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.banner.RescueBanner;
import tw.com.ispan.service.banner.RescueBannerService;

@RestController
@RequestMapping("/rescuebanner")
public class RescueBannerController {

    @Autowired
    private RescueBannerService rescueBannerService;

    /**
     * 新增 RescueBanner
     *
     * @param rescueBanner 要新增的 RescueBanner 物件
     * @return 新增結果
     */
    @PostMapping
    public ResponseEntity<?> createRescueBanner(@RequestBody RescueBanner rescueBanner) {
        if (rescueBanner.getRescueCase() == null || rescueBanner.getRescueCase().getRescueCaseId() == null) {
            return ResponseEntity.badRequest().body("RescueCase ID is required.");
        }

        RescueBanner createdBanner = rescueBannerService.create(rescueBanner);
        return ResponseEntity.ok(createdBanner);
    }

    /**
     * 更新 RescueBanner
     *
     * @param id           Banner 的 ID
     * @param rescueBanner 更新內容
     * @return 更新結果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRescueBanner(@PathVariable Integer id, @RequestBody RescueBanner rescueBanner) {
        if (rescueBanner.getRescueCase() == null || rescueBanner.getRescueCase().getRescueCaseId() == null) {
            return ResponseEntity.badRequest().body("RescueCase ID is required.");
        }

        Optional<RescueBanner> updatedBanner = rescueBannerService.update(id, rescueBanner);
        if (updatedBanner.isPresent()) {
            return ResponseEntity.ok(updatedBanner.get());
        } else {
            return ResponseEntity.status(404).body("LostBanner not found with ID: " + id);
        }
    }

    /**
     * 根據 ID 刪除 RescueBanner
     *
     * @param id Banner 的 ID
     * @return 刪除結果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRescueBanner(@PathVariable Integer id) {
        boolean deleted = rescueBannerService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("LostBanner deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("LostBanner not found with ID: " + id);
        }
    }
}
