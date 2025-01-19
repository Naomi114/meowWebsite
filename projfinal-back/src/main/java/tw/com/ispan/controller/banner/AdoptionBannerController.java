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

// import tw.com.ispan.domain.pet.banner.AdoptionBanner;
// import tw.com.ispan.domain.pet.banner.AdoptionBanner;
// import tw.com.ispan.service.banner.AdoptionBannerService;

// @RestController
// @RequestMapping("/adoptionbanner")
// public class AdoptionBannerController {

// @Autowired
// private AdoptionBannerService adoptionBannerService;

// /**
// * 為 AdoptionCase 創建廣告牆
// */
// @PostMapping
// public ResponseEntity<AdoptionBanner> createBanner(@RequestBody Map<String,
// Object> request) {
// Integer adoptionCaseId = (Integer) request.get("adoptionCaseId");
// LocalDateTime onlineDate = LocalDateTime.parse((String)
// request.get("onlineDate"));
// LocalDateTime dueDate = LocalDateTime.parse((String) request.get("dueDate"));

// AdoptionBanner createdBanner =
// adoptionBannerService.createBannerForAdoptionCase(adoptionCaseId, onlineDate,
// dueDate);
// return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
// }

// /**
// * 根據 AdoptionCase ID 獲取廣告牆
// */
// @GetMapping("/{adoptionCaseId}")
// public ResponseEntity<AdoptionBanner> getBannerByAdoptionCaseId(@PathVariable
// Integer adoptionCaseId) {
// Optional<AdoptionBanner> banner =
// adoptionBannerService.getBannerByAdoptionCaseId(adoptionCaseId);
// return banner.map(ResponseEntity::ok).orElseGet(() ->
// ResponseEntity.notFound().build());
// }

// /**
// * 更新廣告牆日期
// */
// @PutMapping("/{bannerId}")
// public ResponseEntity<AdoptionBanner> updateBannerDates(@PathVariable Integer
// bannerId,
// @RequestBody Map<String, String> request) {
// LocalDateTime newOnlineDate = LocalDateTime.parse(request.get("onlineDate"));
// LocalDateTime newDueDate = LocalDateTime.parse(request.get("dueDate"));

// AdoptionBanner updatedBanner =
// adoptionBannerService.updateBannerDates(bannerId, newOnlineDate, newDueDate);
// return ResponseEntity.ok(updatedBanner);
// }

// /**
// * 刪除廣告牆
// */
// @DeleteMapping("/{bannerId}")
// public ResponseEntity<Void> deleteBanner(@PathVariable Integer bannerId) {
// adoptionBannerService.deleteBanner(bannerId);
// return ResponseEntity.noContent().build();
// }

// }