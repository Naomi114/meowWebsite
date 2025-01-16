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

// import tw.com.ispan.domain.pet.banner.LostBanner;
// import tw.com.ispan.service.banner.LostBannerService;

// @RestController
// @RequestMapping("/lostbanner")
// public class LostBannerController {

// @Autowired
// private LostBannerService lostBannerService;

// /**
// * 新增 LostBanner
// *
// * @param lostBanner LostBanner 資料
// * @return 新增結果
// */
// @PostMapping
// public ResponseEntity<?> createLostBanner(@RequestBody LostBanner lostBanner)
// {
// if (lostBanner.getLostCase() == null ||
// lostBanner.getLostCase().getLostCaseId() == null) {
// return ResponseEntity.badRequest().body("LostCase ID is required.");
// }

// LostBanner createdBanner = lostBannerService.create(lostBanner);
// return ResponseEntity.ok(createdBanner);
// }

// /**
// * 更新 LostBanner
// *
// * @param id Banner ID
// * @param lostBanner 更新內容
// * @return 更新結果
// */
// @PutMapping("/{id}")
// public ResponseEntity<?> updateLostBanner(@PathVariable Integer id,
// @RequestBody LostBanner lostBanner) {
// if (lostBanner.getLostCase() == null ||
// lostBanner.getLostCase().getLostCaseId() == null) {
// return ResponseEntity.badRequest().body("LostCase ID is required.");
// }

// Optional<LostBanner> updatedBanner = lostBannerService.update(id,
// lostBanner);
// if (updatedBanner.isPresent()) {
// return ResponseEntity.ok(updatedBanner.get());
// } else {
// return ResponseEntity.status(404).body("LostBanner not found with ID: " +
// id);
// }
// }

// /**
// * 根據 ID 刪除 LostBanner
// *
// * @param id Banner ID
// * @return 刪除結果
// */
// @DeleteMapping("/{id}")
// public ResponseEntity<?> deleteLostBanner(@PathVariable Integer id) {
// boolean deleted = lostBannerService.deleteById(id);
// if (deleted) {
// return ResponseEntity.ok("LostBanner deleted successfully.");
// } else {
// return ResponseEntity.status(404).body("LostBanner not found with ID: " +
// id);
// }
// }
// }
