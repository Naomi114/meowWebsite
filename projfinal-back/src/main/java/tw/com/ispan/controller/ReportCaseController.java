package tw.com.ispan.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.service.pet.ReportCaseService;

@RestController
@RequestMapping("/reports")
public class ReportCaseController {

    @Autowired
    private ReportCaseService reportCaseService;

    // 1. 新增 ReportCase
    @PostMapping
    public ReportCase createReportCase(@RequestBody ReportCase reportCase) {
        reportCase.setReportDate(LocalDateTime.now()); // 設定當前時間為報告日期
        return reportCaseService.save(reportCase);
    }

    // 2. 查詢所有 ReportCases
    @GetMapping
    public List<ReportCase> getAllReportCases() {
        return reportCaseService.findAll();
    }

    // 3. 根據 ID 查詢 ReportCase
    @GetMapping("/{id}")
    public ReportCase getReportCaseById(@PathVariable Integer id) {
        Optional<ReportCase> reportCase = reportCaseService.findById(id);
        if (reportCase.isPresent()) {
            return reportCase.get();
        } else {
            throw new IllegalArgumentException("找不到指定的 ReportCase，ID: " + id);
        }
    }

    // /**
    // * 根據 memberId 查詢 ReportCase
    // *
    // * @param memberId 會員的 ID
    // * @return 與該會員相關的 ReportCase 列表
    // */
    // @GetMapping("/{memberId}")
    // public ResponseEntity<?> getReportCasesByMemberId(@PathVariable Integer
    // memberId) {
    // List<ReportCase> reportCases = reportCaseService.findByMemberId(memberId);
    // if (!reportCases.isEmpty()) {
    // return ResponseEntity.ok(reportCases);
    // } else {
    // return ResponseEntity.status(404).body("No ReportCases found for memberId: "
    // + memberId);
    // }
    // }

    // 4. 更新 ReportCase
    @PutMapping("/{id}")
    public ReportCase updateReportCase(@PathVariable Integer id, @RequestBody ReportCase reportCase) {
        Optional<ReportCase> existingReportCase = reportCaseService.findById(id);
        if (existingReportCase.isPresent()) {
            ReportCase update = existingReportCase.get();
            // update.setRescueCase(reportCase.getRescueCase());
            update.setLostCase(reportCase.getLostCase());
            // update.setAdoptionCase(reportCase.getAdoptionCase());
            // update.setMember(reportCase.getMember());
            update.setReportType(reportCase.getReportType());
            update.setReportNotes(reportCase.getReportNotes());
            return reportCaseService.save(update);
        } else {
            throw new IllegalArgumentException("找不到指定的 ReportCase，ID: " + id);
        }
    }

    // 5. 刪除 ReportCase
    @DeleteMapping("/{id}")
    public String deleteReportCase(@PathVariable Integer id) {
        if (reportCaseService.existsById(id)) {
            reportCaseService.deleteById(id);
            return "刪除成功，ID: " + id;
        } else {
            return "找不到指定的 ReportCase，ID: " + id;
        }
    }
}
