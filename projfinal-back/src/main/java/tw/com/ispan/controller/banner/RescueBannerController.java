// package tw.com.ispan.controller.banner;

// import java.time.LocalDateTime;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import tw.com.ispan.domain.pet.banner.RescueBanner;
// import tw.com.ispan.service.banner.RescueBannerService;

// @RestController
// @RequestMapping("/rescuebanner")
// public class RescueBannerController {

//     @Autowired
//     private RescueBannerService rescueBannerService;

//     /**
//      * 為 RescueCase 創建廣告牆
//      */
//     @PostMapping
//     public ResponseEntity<RescueBanner> createBanner(@RequestBody Map<String, Object> request) {
//         Integer rescueCaseId = (Integer) request.get("rescueCaseId");
//         LocalDateTime onlineDate = LocalDateTime.parse((String) request.get("onlineDate"));
//         LocalDateTime dueDate = LocalDateTime.parse((String) request.get("dueDate"));

//         RescueBanner createdBanner = rescueBannerService.createBannerForRescueCase(rescueCaseId, onlineDate, dueDate);
//         return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
//     }

//     /**
//      * 根據 RescueCase ID 獲取廣告牆
//      */
//     @GetMapping("/{rescueCaseId}")
//     public ResponseEntity<RescueBanner> getBannerByRescueCaseId(@PathVariable Integer rescueCaseId) {
//         Optional<RescueBanner> banner = rescueBannerService.getBannerByRescueCaseId(rescueCaseId);
//         return banner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     /**
//      * 更新廣告牆日期
//      */
//     @PutMapping("/{bannerId}")
//     public ResponseEntity<RescueBanner> updateBannerDates(@PathVariable Integer bannerId,
//             @RequestBody Map<String, String> request) {
//         LocalDateTime newOnlineDate = LocalDateTime.parse(request.get("onlineDate"));
//         LocalDateTime newDueDate = LocalDateTime.parse(request.get("dueDate"));

//         RescueBanner updatedBanner = rescueBannerService.updateBannerDates(bannerId, newOnlineDate, newDueDate);
//         return ResponseEntity.ok(updatedBanner);
//     }

//     /**
//      * 刪除廣告牆
//      */
//     @DeleteMapping("/{bannerId}")
//     public ResponseEntity<Void> deleteBanner(@PathVariable Integer bannerId) {
//         rescueBannerService.deleteBanner(bannerId);
//         return ResponseEntity.noContent().build();
//     }
// }
