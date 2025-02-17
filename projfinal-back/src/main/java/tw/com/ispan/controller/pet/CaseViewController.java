package tw.com.ispan.controller.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.CaseView;
import tw.com.ispan.service.pet.CaseViewService;

@RestController
@RequestMapping("/api/caseView")
public class CaseViewController {
    @Autowired
    private CaseViewService caseViewService;

    // 用戶進入案件頁面當下，紀錄案件瀏覽
    @PostMapping("/record")
    public void recordView(@RequestBody Map<String, Object> requestData) {
        String caseType = (String) requestData.get("caseType");
        Integer caseId = (Integer) requestData.get("caseId");
        caseViewService.recordView(caseType, caseId);
    }

    // 獲取前 10 名瀏覽次數最高的案件
    @GetMapping("/top/{caseType}")
    public List<Map<String, Object>> getTopViewedCases(@PathVariable String caseType) {
        return caseViewService.getTopViewedCases(caseType);
    }

    // 獲取後 10 名瀏覽次數最低的案件
    @GetMapping("/bottom/{caseType}")
    public List<Map<String, Object>> getBottomViewedCases(@PathVariable String caseType) {
        return caseViewService.getBottomViewedCases(caseType);
    }
    
 // 獲取 單一案件在不同時間點的瀏覽趨勢
    @GetMapping("/trend/{caseType}/{caseId}")
    public List<Map<String, Object>> getCaseViewTrend(
            @PathVariable String caseType,
            @PathVariable Integer caseId) {
        System.out.println("進來了~~");
    	return caseViewService.getCaseViewTrend(caseType, caseId);
    }
}
