package tw.com.ispan.controller.pet.forRescue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.forRescue.RescueProgress;
import tw.com.ispan.dto.pet.RescueProgressDTO;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.service.pet.ImageService;
import tw.com.ispan.service.pet.RescueCaseService;
import tw.com.ispan.service.pet.forRescue.RescueProgressService;

@RestController
@RequestMapping(path = { "/api/RescueCase/rescueProgress" })
public class RescueProgressController {

	@Value("${back.domainName.url}") // http://localhost:8080
	private String domainName;
	@Autowired
	private RescueProgressService rescueProgressService;
	@Autowired
	private RescueCaseService rescueCaseService;
	@Autowired
	private RescueCaseRepository rescueCaseRepository;
	@Autowired
	private ImageService imageService;

	// 新增某案件的救援進度
	@PostMapping("/add/{rescueCaseId}")
	public ResponseEntity<String> addRescueProgress(@PathVariable Integer rescueCaseId,
			@RequestBody RescueProgressDTO progressDTO) {

		// 先透過 caseID 找到對應的 RescueCase
		Optional<RescueCase> rescueCaseOptional = rescueCaseRepository.findById(rescueCaseId);
		if (rescueCaseOptional.isEmpty()) {
			return ResponseEntity.badRequest().body("找不到對應的救援案件");
		}

		RescueCase rescueCase = rescueCaseOptional.get();

		RescueProgress rescueProgress = new RescueProgress();
		rescueProgress.setRescueCase(rescueCase); // 設定關聯案件
		rescueProgress.setProgressDetail(progressDTO.getProgressDetail());
		rescueProgress.setCreateTime(LocalDateTime.now()); // 自動填充當前時間

		// 圖片要先轉換為後端路徑(前端會傳來一個字串集合，但限制只會有一張圖)
		if (progressDTO.getImageUrl() != null && !progressDTO.getImageUrl().isEmpty()) {
			String progressImage = progressDTO.getImageUrl().get(0); // 確保有元素後才取

			// 圖片需要動到永存資料夾，同時改路徑在存入救援進度資料表
			System.out.println("抓到前端傳來圖片路徑" + progressDTO.getImageUrl());
			String finalProgressImage = imageService.moveImage(progressImage);
			rescueProgress.setImageUrl(finalProgressImage);
		}

		rescueProgressService.addProgress(rescueProgress);

		return ResponseEntity.ok("進度更新成功");
	}

	// 根據案件id搜尋該救援進度
	@GetMapping("/{caseId}")
	public ResponseEntity<List<RescueProgress>> getProgressByCaseId(@PathVariable Integer caseId) {
		List<RescueProgress> progressList = rescueProgressService.getProgressByCaseId(caseId);

		// 修改圖片路徑，將 `C:/upload/final/pet/images/...` 轉換為前端可訪問
		// `http://localhost:5173/upload/final/pet/images/...`
		String baseUrl = domainName + "/upload/final/pet/images/";

		for (RescueProgress progress : progressList) {
			if (progress.getImageUrl() != null && !progress.getImageUrl().isEmpty()) {
				// 取得圖片檔案名稱
				String imageFileName = progress.getImageUrl().substring(progress.getImageUrl().lastIndexOf("/") + 1);

				// 重新組合可訪問的 URL
				progress.setImageUrl(baseUrl + imageFileName);
			}
		}

		return ResponseEntity.ok(progressList);
	}

	// 根據案件ID 和 progressId 查詢特定的進度
	@GetMapping("/{caseId}/{progressId}")
	public ResponseEntity<RescueProgress> getProgressByCaseIdAndProgressId(
			@PathVariable Integer caseId, @PathVariable Integer progressId) {

		Optional<RescueProgress> progressOptional = rescueProgressService
				.getProgressByCaseIdAndProgressId(caseId, progressId);

		if (progressOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		RescueProgress progress = progressOptional.get();

		// 修改圖片路徑，將 `C:/upload/final/pet/images/...` 轉換為
		// `http://localhost:8080/upload/...`
		if (progress.getImageUrl() != null && !progress.getImageUrl().isEmpty()) {
			String baseUrl = domainName + "/upload/final/pet/images/";
			String imageFileName = progress.getImageUrl().substring(progress.getImageUrl().lastIndexOf("/") + 1);
			progress.setImageUrl(baseUrl + imageFileName);
		}

		return ResponseEntity.ok(progress);
	}

	// 更新特定進度
	@PutMapping("/{caseId}/{progressId}")
	public ResponseEntity<?> updateRescueProgress(
			@PathVariable Integer caseId,
			@PathVariable Integer progressId,
			@RequestBody RescueProgress updatedProgress) {

		Optional<RescueProgress> progressOptional = rescueProgressService.getProgressByCaseIdAndProgressId(caseId,
				progressId);
		if (!progressOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		RescueProgress progress = progressOptional.get();
		progress.setProgressDetail(updatedProgress.getProgressDetail());

		String finalProgressImage = imageService.moveImage(updatedProgress.getImageUrl());
		progress.setImageUrl(finalProgressImage);

		RescueProgress savedProgress = rescueProgressService.updateRescueProgress(progress);
		return ResponseEntity.ok(savedProgress);
	}
}
