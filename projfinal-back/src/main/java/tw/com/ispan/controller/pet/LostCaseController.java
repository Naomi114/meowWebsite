package tw.com.ispan.controller.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.dto.pet.LostSearchCriteria;
import tw.com.ispan.dto.pet.OutputLostCaseDTO;
import tw.com.ispan.service.pet.ImageService;
import tw.com.ispan.service.pet.LostCaseService;

@RestController
@CrossOrigin
@RequestMapping("/api/lostcases")
public class LostCaseController {
    @Autowired
    private LostCaseService lostCaseService;

    @Autowired
    private ImageService imageService;

    @Value("${back.domainName.url}")
    private String backDomainName;

    @Value("${file.petUpload.path}")
    private String petUploadPath;

    /**
     * 根據會員 ID 查詢對應的 LostCases
     *
     * @param memberId 會員 ID
     * @return 符合條件的 LostCase 列表
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LostCase>> getLostCasesByMemberId(@PathVariable Integer memberId) {
        System.out.println("🔍 取得的 memberId：" + memberId); // 確認是否正確解析
        List<LostCase> lostCases = lostCaseService.findByMemberId(memberId);
        return ResponseEntity.ok(lostCases);
    }

    /**
     * 更新 LostCase 的資訊
     * 
     * @param lostCaseId 走失案件的 ID
     * @param json       請求體，包含要更新的欄位
     * @return 更新後的 LostCase
     */
    @PutMapping("/{lostCaseId}")
    public ResponseEntity<LostCase> updateLostCase(
            @PathVariable Integer lostCaseId,
            @RequestBody String json) {

        JSONObject param = new JSONObject(json);
        LostCase updatedLostCase = lostCaseService.modify(lostCaseId, param);

        return ResponseEntity.ok(updatedLostCase);
    }

    /**
     * 根據 ID 刪除 LostCase，並刪除對應的 Banner
     * 
     * @param lostCaseId 走失案件的 ID
     * @return 成功刪除返回 204 No Content
     */
    @DeleteMapping("/{lostCaseId}")
    public ResponseEntity<Void> deleteLostCase(@PathVariable Integer lostCaseId) {
        lostCaseService.delete(lostCaseId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * 根據 ID 查詢 LostCase
     *
     * @param lostCaseId 走失案件的 ID
     * @return LostCase 的資料 (DTO)
     */
    @GetMapping("/{lostCaseId}")
    public ResponseEntity<OutputLostCaseDTO> getLostCaseById(@PathVariable Integer lostCaseId) {
        Optional<OutputLostCaseDTO> lostCaseDTO = lostCaseService.findById(lostCaseId);

        return lostCaseDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 創建 LostCase 並自動創建對應的 Banner
     *
     * @param json 請求體，包含 LostCase 的資訊
     * @return 創建的 LostCase
     */
    @PostMapping("/create")
    public ResponseEntity<LostCase> createLostCase(@RequestBody String json) {
        JSONObject param = new JSONObject(json);

        List<String> imagePaths = param.getJSONArray("images").toList().stream()
                .map(Object::toString)
                .toList();
        List<String> finalImageUrls = imageService.moveImages(imagePaths);
        System.out.println("圖片移動完成: " + finalImageUrls);
        List<CasePicture> casePictures = imageService.saveImage(finalImageUrls);

        LostCase createdLostCase = lostCaseService.create(param, casePictures);

        return ResponseEntity.ok(createdLostCase);
    }

    /**
     * 🔍 搜尋失蹤案件（支援關鍵字模糊查詢 + 篩選條件）
     */
    @PostMapping("/search")
    public ResponseEntity<Page<LostCase>> searchLostCases(@RequestBody @Valid LostSearchCriteria criteria) {
        try {
            System.out.println("🔎 收到搜尋請求：" + criteria.toString());
            Page<LostCase> result = lostCaseService.searchLostCases(criteria);

            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println("❌ 查詢失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 取得所有遺失案件（支援排序）
     *
     * @param dir 排序方向 (true = desc, false = asc)
     * @return 遺失案件列表 (DTO)
     */
    @GetMapping("/all")
    public List<OutputLostCaseDTO> getAllLostCases(@RequestParam(defaultValue = "true") boolean dir) {
        return lostCaseService.getAll(dir);
    }

    // 給GOOGLEMAP使用
    @GetMapping("/getLocations")
    public List<Map<String, Object>> getLostCasesLocations() {
        List<LostCase> cases = lostCaseService.getAllCases();

        List<Map<String, Object>> response = new ArrayList<>();
        for (LostCase lostCase : cases) {
            Map<String, Object> caseData = new HashMap<>();
            caseData.put("caseTitle", lostCase.getCaseTitle());
            caseData.put("latitude", lostCase.getLatitude());
            caseData.put("longitude", lostCase.getLongitude());
            caseData.put("rescueReason", lostCase.getFeatureDescription());
            caseData.put("publicationTime", lostCase.getPublicationTime());
            caseData.put("city", lostCase.getCity().getCity());
            caseData.put("district", lostCase.getDistrictArea().getDistrictAreaName());
            caseData.put("caseState", lostCase.getCaseState());
            caseData.put("caseId", lostCase.getLostCaseId());
            caseData.put("caseType", "lostCase");

            // 修正 casePictures 中的 pictureUrl，確保前端可以訪問
            List<Map<String, String>> fixedCasePictures = new ArrayList<>();
            for (CasePicture picture : lostCase.getCasePictures()) {
                Map<String, String> pictureData = new HashMap<>();
                String originalPath = picture.getPictureUrl(); // 取得原始路徑
                String fixedPath = originalPath.replace(petUploadPath, backDomainName + "/upload/"); // 替換成可訪問 URL
                pictureData.put("pictureUrl", fixedPath);
                fixedCasePictures.add(pictureData);
            }

            caseData.put("casePictures", fixedCasePictures); // 更新處理後的圖片路徑

            response.add(caseData);
        }

        return response;
    }

}
