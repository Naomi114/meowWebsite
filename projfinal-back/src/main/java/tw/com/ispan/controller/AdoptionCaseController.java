package tw.com.ispan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.dto.AdoptioncaseDTO;
import tw.com.ispan.service.pet.AdoptionCaseService;

@RestController
@RequestMapping("/adoptionsearch")
public class AdoptionCaseController {

    @Autowired
    private final AdoptionCaseService adoptionCaseService;

    public AdoptionCaseController(AdoptionCaseService adoptionCaseService) {
        this.adoptionCaseService = adoptionCaseService;
    }

    // 創建新的 AdoptionCase
    @PostMapping
    public ResponseEntity<?> createAdoptionCase(@RequestBody AdoptionCase adoptionCase) {
        try {
            // 調用服務層的方法來創建 AdoptionCase
            AdoptionCase createdAdoptionCase = adoptionCaseService.createAdoptionCase(adoptionCase);
            // 返回創建的 AdoptionCase 並返回 201 (Created) 狀態碼
            return new ResponseEntity<>(createdAdoptionCase, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // 如果有錯誤，返回 400 (Bad Request) 狀態碼和錯誤訊息
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 捕獲其他可能的錯誤，返回 500 (Internal Server Error) 狀態碼
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 更新 AdoptionCase 的 status 和 note
    @PutMapping("/{adoptionCaseId}")
    public AdoptionCase updateAdoptionCase(@PathVariable Integer adoptionCaseId,
            @RequestBody AdoptioncaseDTO dto) {
        return adoptionCaseService.updateAdoptionCaseStatusAndNote(adoptionCaseId, dto);
    }

    // 查詢
    @GetMapping("/search")
    public List<AdoptionCase> searchAdoptionCases(
            @RequestParam(value = "cityId", required = false) Long cityId,
            @RequestParam(value = "distinctAreaId", required = false) Long distinctAreaId,
            @RequestParam(value = "caseStateId", required = false) Long caseStateId,
            @RequestParam(value = "speciesId", required = false) Long speciesId,
            @RequestParam(value = "gender", required = false) String gender) {
        return adoptionCaseService.searchAdoptionCases(cityId, distinctAreaId, caseStateId, speciesId, gender);
    }
}
