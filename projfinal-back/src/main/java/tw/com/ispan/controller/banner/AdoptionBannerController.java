// package tw.com.ispan.controller.banner;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import tw.com.ispan.domain.pet.banner.AdoptionBanner;
// import tw.com.ispan.service.banner.AdoptionBannerService;

// @RestController
// @RequestMapping("/adoptionbanner")
// public class AdoptionBannerController {

// @Autowired
// private AdoptionBannerService adoptionBannerService;

// /**
// * 新增 AdoptionBanner
// *
// * @param adoptionBanner 要新增的 AdoptionBanner 物件
// * @return 新增結果
// */
// @PostMapping
// public ResponseEntity<?> createAdoptionBanner(@RequestBody AdoptionBanner
// adoptionBanner) {
// if (adoptionBanner.getAdoptionCase() == null ||
// adoptionBanner.getAdoptionCase().getAdoptionCaseId() == null) {
// return ResponseEntity.badRequest().body("AdoptionCase ID is required.");
// }
// AdoptionBanner createdBanner = adoptionBannerService.create(adoptionBanner);
// return ResponseEntity.ok(createdBanner);
// }

// /**
// * 更新 AdoptionBanner
// *
// * @param id Banner 的 ID
// * @param adoptionBanner 更新內容
// * @return 更新結果
// */
// @PutMapping("/{id}")
// public ResponseEntity<?> updateAdoptionBanner(@PathVariable Integer id,
// @RequestBody AdoptionBanner adoptionBanner) {
// if (adoptionBanner.getAdoptionCase() == null ||
// adoptionBanner.getAdoptionCase().getAdoptionCaseId() == null) {
// return ResponseEntity.badRequest().body("AdoptionCase ID is required.");
// }
// Optional<AdoptionBanner> updatedBanner = adoptionBannerService.update(id,
// adoptionBanner);
// if (updatedBanner.isPresent()) {
// return ResponseEntity.ok(updatedBanner.get());
// } else {
// return ResponseEntity.status(404).body("AdoptionBanner not found with ID: " +
// id);
// }
// }

// /**
// * 根據 ID 刪除 AdoptionBanner
// *
// * @param id Banner 的 ID
// * @return 刪除結果
// */
// @DeleteMapping("/{id}")
// public ResponseEntity<?> deleteAdoptionBanner(@PathVariable Integer id) {
// boolean deleted = adoptionBannerService.deleteById(id);
// if (deleted) {
// return ResponseEntity.ok("AdoptionBanner deleted successfully.");
// } else {
// return ResponseEntity.status(404).body("AdoptionBanner not found with ID: " +
// id);
// }
// }

// }