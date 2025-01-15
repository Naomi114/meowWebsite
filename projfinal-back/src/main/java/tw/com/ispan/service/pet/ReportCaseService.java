package tw.com.ispan.service.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.repository.pet.ReportCaseRepository;

@Service
public class ReportCaseService {

    @Autowired
    private ReportCaseRepository reportCaseRepository;

    /**
     * 新增或更新 ReportCase
     *
     * @param reportCase ReportCase 物件
     * @return 儲存後的 ReportCase 物件
     */
    public ReportCase save(ReportCase reportCase) {
        if (reportCase == null) {
            throw new IllegalArgumentException("ReportCase 物件不能為 null");
        }

        // 新增操作（ID 為 null 時）
        if (reportCase.getReportId() == null) {
            System.out.println("執行新增操作...");
        } else {
            // 更新操作（ID 存在時）
            Optional<ReportCase> existing = reportCaseRepository.findById(reportCase.getReportId());
            if (existing.isPresent()) {
                System.out.println("執行更新操作...");
                ReportCase existingReportCase = existing.get();

                // 更新非空欄位值
                existingReportCase.setRescueCase(reportCase.getRescueCase());
                existingReportCase.setLostCase(reportCase.getLostCase());
                existingReportCase.setAdoptionCase(reportCase.getAdoptionCase());
                existingReportCase.setMember(reportCase.getMember());
                existingReportCase.setReportDate(reportCase.getReportDate());
                existingReportCase.setReportType(reportCase.getReportType());
                existingReportCase.setReportNotes(reportCase.getReportNotes());

                // 儲存更新後的 ReportCase
                return reportCaseRepository.save(existingReportCase);
            } else {
                throw new IllegalArgumentException("要更新的 ReportCase 不存在，ID: " + reportCase.getReportId());
            }
        }

        // 儲存新增的 ReportCase
        return reportCaseRepository.save(reportCase);
    }

    /**
     * 查詢所有 ReportCase
     *
     * @return 所有 ReportCase 的列表
     */
    public List<ReportCase> findAll() {
        return reportCaseRepository.findAll();
    }

    /**
     * 根據 ID 查詢 ReportCase
     *
     * @param id ReportCase 的 ID
     * @return Optional 包裝的 ReportCase
     */
    public Optional<ReportCase> findById(Integer id) {
        return reportCaseRepository.findById(id);
    }

    /**
     * 根據 ID 刪除 ReportCase
     *
     * @param id 要刪除的 ReportCase 的 ID
     */
    public void deleteById(Integer id) {
        reportCaseRepository.deleteById(id);
    }

    /**
     * 確認 ReportCase 是否存在
     *
     * @param id 要檢查的 ReportCase ID
     * @return 是否存在
     */
    public boolean existsById(Integer id) {
        return reportCaseRepository.existsById(id);
    }

    // /**
    // * 根據 ReportType 查詢 ReportCase
    // *
    // * @param reportType 報告類型
    // * @return 符合條件的 ReportCase 列表
    // */
    // public List<ReportCase> findByReportType(String reportType) {
    // return reportCaseRepository.findByReportType(reportType);
    // }

    /**
     * 根據 Member ID 查詢相關的 ReportCase
     *
     * @param memberId 會員 ID
     * @return 符合條件的 ReportCase 列表
     */
    public List<ReportCase> findByMemberId(Integer memberId) {
        return reportCaseRepository.findByMemberId(memberId);
    }

    /**
     * 根據 LostCase ID 查詢相關的 ReportCase
     *
     * @param lostCaseId 遺失案件 ID
     * @return 符合條件的 ReportCase 列表
     */
    public List<ReportCase> findByLostCaseId(Integer lostCaseId) {
        return reportCaseRepository.findByLostCaseId(lostCaseId);
    }

    /**
     * 根據 RescueCase ID 查詢相關的 ReportCase
     *
     * @param rescueCaseId 遺失案件 ID
     * @return 符合條件的 ReportCase 列表
     */
    public List<ReportCase> findByRescueCaseId(Integer rescueCaseId) {
        return reportCaseRepository.findByRescueCaseId(rescueCaseId);
    }

    /**
     * 根據 AdoptionCase ID 查詢相關的 ReportCase
     *
     * @param adoptionCaseId 遺失案件 ID
     * @return 符合條件的 ReportCase 列表
     */
    public List<ReportCase> findByAdoptionCaseId(Integer adoptionCaseId) {
        return reportCaseRepository.findByAdoptionCaseId(adoptionCaseId);
    }

    /**
     * 查詢報告數量
     *
     * @return ReportCase 的總數量
     */
    public long count() {
        return reportCaseRepository.count();
    }

    /**
     * 刪除所有 ReportCase
     */
    public void deleteAll() {
        reportCaseRepository.deleteAll();
    }
}
