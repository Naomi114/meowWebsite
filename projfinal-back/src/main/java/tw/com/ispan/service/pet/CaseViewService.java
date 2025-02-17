package tw.com.ispan.service.pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.CaseView;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.repository.pet.CaseViewRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseRepository;

@Service
public class CaseViewService {

    @Autowired
    private CaseViewRepository caseViewRepository;
    @Autowired
    private RescueCaseRepository rescueCaseRepository;
    @Autowired
    private LostCaseRepository lostCaseRepository;
    @Autowired
    private AdoptionCaseRepository adoptionCaseRepository;

    // 新增案件瀏覽紀錄
    public void recordView(String caseType, Integer caseId) {
        CaseView caseView = new CaseView();

        switch (caseType) {
            case "rescue":
                RescueCase rescueCase = rescueCaseRepository.findById(caseId).orElse(null);
                caseView.setRescueCase(rescueCase);
                break;
            case "lost":
                LostCase lostCase = lostCaseRepository.findById(caseId).orElse(null);
                caseView.setLostCase(lostCase);
                break;
            case "adoption":
                AdoptionCase adoptionCase = adoptionCaseRepository.findById(caseId).orElse(null);
                caseView.setAdoptionCase(adoptionCase);
                break;
            default:
                throw new IllegalArgumentException("未知的案件類型: " + caseType);
        }

        caseViewRepository.save(caseView);
    }

    // 取得前10名案件
    public List<Map<String, Object>> getTopViewedCases(String caseType) {
        List<Object[]> results;
        switch (caseType.toLowerCase()) {
            case "rescue":
                results = caseViewRepository.findTopViewedRescueCases();
                break;
            case "lost":
                results = caseViewRepository.findTopViewedLostCases();
                break;
            case "adoption":
                results = caseViewRepository.findTopViewedAdoptionCases();
                break;
            default:
                throw new IllegalArgumentException("無效的案件類型：" + caseType);
        }
        return convertToCaseList(results);
    }

    // 取得後 10名案件
    public List<Map<String, Object>> getBottomViewedCases(String caseType) {
        List<Object[]> results;
        switch (caseType.toLowerCase()) {
            case "rescue":
                results = caseViewRepository.findBottomViewedRescueCases();
                break;
            case "lost":
                results = caseViewRepository.findBottomViewedLostCases();
                break;
            case "adoption":
                results = caseViewRepository.findBottomViewedAdoptionCases();
                break;
            default:
                throw new IllegalArgumentException("無效的案件類型：" + caseType);
        }
        return convertToCaseList(results);
    }

    // 轉換查詢結果
    private List<Map<String, Object>> convertToCaseList(List<Object[]> cases) {
        return cases.stream().map(row -> {
            Map<String, Object> caseInfo = new HashMap<>();
            caseInfo.put("caseId", row[0]);
            caseInfo.put("caseTitle", row[1]);
            caseInfo.put("viewCount", row[2]);
            return caseInfo;
        }).collect(Collectors.toList());
    }

    // 取得單一案件在不同時間點的瀏覽趨勢
    public List<Map<String, Object>> getCaseViewTrend(String caseType, Integer caseId) {
        List<Object[]> results = caseViewRepository.findCaseViewTrend(caseType, caseId);
        return results.stream().map(row -> {
            Map<String, Object> viewData = new HashMap<>();
            viewData.put("viewDate", row[0].toString()); // 只返回 YYYY-MM-DD 格式
            viewData.put("viewCount", row[1]); // 該時間段的瀏覽數
            return viewData;
        }).collect(Collectors.toList());
    }

}
