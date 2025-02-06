package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.forAdopt.AdoptionCaseApply;
import tw.com.ispan.dto.AdoptionCaseApplyDTO;
import tw.com.ispan.service.pet.AdoptionCaseApplyService;

@RestController
@RequestMapping("/apply/create")
public class AdoptionCaseApplyController {

    @Autowired
    private AdoptionCaseApplyService adoptionCaseApplyService;

    // 創建新的申請
    @PostMapping
    public ResponseEntity<AdoptionCaseApply> createAdoptionCaseApply(
            @RequestBody AdoptionCaseApplyDTO adoptionCaseApplyDTO) {
        try {
            // 調用 Service 層來創建申請
            AdoptionCaseApply savedApply = adoptionCaseApplyService.createAdoptionCaseApply(adoptionCaseApplyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedApply); // 回傳 201 和創建的申請資料
        } catch (IllegalArgumentException e) {
            // 處理錯誤，如 Member 或 AdoptionCase 沒找到
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 回傳 400 錯誤
        }
    }
}