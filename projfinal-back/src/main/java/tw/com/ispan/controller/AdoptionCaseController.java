package tw.com.ispan.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private AdoptionCaseService adoptionCaseService;

    //     System.out.println() 和 e.printStackTrace() 可能被日誌框架攔截
    // 🔹 問題

    // Spring Boot 默認使用 SLF4J + Logback 來處理日誌。
    // System.out.println() 和 e.printStackTrace() 可能被 Logback 覆蓋，導致 Console 沒有輸出
    private static final Logger logger = LoggerFactory.getLogger(AdoptionCaseController.class);

    // 創建新的 AdoptionCase
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAdoptionCase(@RequestBody AdoptioncaseDTO dto) {
        try {
            System.out.println("收到的 JSON: " + dto);
            AdoptionCase createdAdoptionCase = adoptionCaseService.createAdoptionCase(dto);
            return new ResponseEntity<>(createdAdoptionCase, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); 
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 更新註記 status 和 note
    @PutMapping("/{adoptionCaseId}")
    public AdoptionCase updateAdoptionCase(@PathVariable Integer adoptionCaseId,
            @RequestBody AdoptioncaseDTO dto) {
        return adoptionCaseService.updateAdoptionCaseStatusAndNote(adoptionCaseId, dto);
    }

    // 查詢
    @GetMapping("/search")
    public List<AdoptionCase> searchAdoptionCases(
            @RequestParam(value = "cityId", required = false) Long cityId,
            @RequestParam(value = "districtAreaId", required = false) Long districtAreaId,
            @RequestParam(value = "caseStateId", required = false) Long caseStateId,
            @RequestParam(value = "speciesId", required = false) Long speciesId,
            @RequestParam(value = "gender", required = false) String gender) {
        return adoptionCaseService.searchAdoptionCases(cityId, districtAreaId, caseStateId, speciesId, gender);
    }

   

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("請求格式錯誤: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("請求格式錯誤: " + ex.getMessage());
    }
}
