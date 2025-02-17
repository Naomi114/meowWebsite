package tw.com.ispan.controller.pet;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.dto.pet.AdoptioncaseDTO;
import tw.com.ispan.service.pet.AdoptionCaseService;

@RestController
@RequestMapping("/api/adoptionsearch")
public class AdoptionCaseController {

    @Autowired
    private AdoptionCaseService adoptionCaseService;

    // 創建新的 AdoptionCase
    @PostMapping("/create")
    public ResponseEntity<AdoptionCase> createAdoptionCase(@RequestBody String json) {
        System.out.println("接收到的 AdoptionCase: " + json);
        JSONObject param = new JSONObject(json);
        AdoptionCase createAdoptionCase = adoptionCaseService.createAdoptionCase(param);
        return ResponseEntity.ok(createAdoptionCase);
    }

    // 更新註記 status 和 note
    @PutMapping("/note/{adoptionCaseId}")
    public AdoptionCase updateAdoptionCase(@PathVariable Integer adoptionCaseId,
            @RequestBody AdoptioncaseDTO dto) {
        return adoptionCaseService.updateAdoptionCaseStatusAndNote(adoptionCaseId, dto);
    }

    // 查詢
    @PostMapping("/search")
    public ResponseEntity<Page<AdoptionCase>> AdoptionCases(@RequestBody String json) {
        JSONObject param = new JSONObject(json);
        Page<AdoptionCase> cases = adoptionCaseService.searchAdoptionCases(param);
        return ResponseEntity.ok(cases);
    }

    // 更新資料
    @PutMapping("/{adoptionCaseId}")
    public ResponseEntity<AdoptionCase> update(
            @PathVariable Integer adoptionCaseId,
            @RequestBody String json) {

        JSONObject param = new JSONObject(json);
        AdoptionCase updateadoptionCase = adoptionCaseService.modify(adoptionCaseId, param);

        return ResponseEntity.ok(updateadoptionCase);
    }

    // 刪除
    @DeleteMapping("/{adoptionCaseId}")
    public ResponseEntity<Void> delete(@PathVariable Integer adoptionCaseId) {
        adoptionCaseService.delete(adoptionCaseId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 查詢一筆
    @GetMapping("/{adoptionCaseId}")
    public ResponseEntity<AdoptionCase> getAdoptionCaseIdById(@PathVariable Integer adoptionCaseId) {
        Optional<AdoptionCase> adoptionCase = adoptionCaseService.findById(adoptionCaseId);
        return adoptionCase.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
