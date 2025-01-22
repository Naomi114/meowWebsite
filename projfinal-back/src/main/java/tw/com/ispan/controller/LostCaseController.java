package tw.com.ispan.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.dto.LostCaseResponse;
import tw.com.ispan.service.pet.LostCaseService;

@RestController
@RequestMapping("/lostcases")
public class LostCaseController {
    @Autowired
    private LostCaseService lostCaseService;

    /**
     * 更新 LostCase 的資訊
     * 
     * @param lostCaseId 走失案件的 ID
     * @param json       請求體，包含要更新的欄位
     * @return 更新後的 LostCase
     */
    @PutMapping("/{Id}")
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
    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteLostCase(@PathVariable Integer lostCaseId) {
        lostCaseService.delete(lostCaseId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * 根據 ID 查詢 LostCase
     * 
     * @param lostCaseId 走失案件的 ID
     * @return LostCase 的資料
     */
    @GetMapping("/{Id}")
    public ResponseEntity<LostCase> getLostCaseById(@PathVariable Integer lostCaseId) {
        Optional<LostCase> lostCase = lostCaseService.findById(lostCaseId);
        return lostCase.map(ResponseEntity::ok)
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
        LostCase createdLostCase = lostCaseService.create(param);
        return ResponseEntity.ok(createdLostCase);
    }

    /**
     * 查詢所有 LostCase，支援模糊查詢
     */
    @PostMapping("/search")
    public ResponseEntity<List<LostCase>> searchLostCases(@RequestBody String json) {
        JSONObject param = new JSONObject(json);
        List<LostCase> cases = lostCaseService.searchLostCases(param);
        return ResponseEntity.ok(cases);
    }

    @PostMapping("/find")
    public LostCaseResponse find(@RequestBody String json) {
        LostCaseResponse responseBean = new LostCaseResponse();

        long count = lostCaseService.count(json);
        responseBean.setCount(count);

        List<LostCase> lostCases = lostCaseService.find(json);
        if (lostCases != null && !lostCases.isEmpty()) {
            responseBean.setList(lostCases);
        } else {
            responseBean.setList(new ArrayList<>());
        }

        return responseBean;
    }
}
