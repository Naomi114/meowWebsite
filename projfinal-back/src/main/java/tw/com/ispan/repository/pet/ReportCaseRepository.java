package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.ReportCase;

@Repository
public interface ReportCaseRepository extends JpaRepository<ReportCase, Integer>, ReportCaseDAO {
    // 根據 Case ID 查詢
    List<ReportCase> findByLostCase_LostCaseId(Integer lostCaseId);

    List<ReportCase> findByRescueCase_RescueCaseId(Integer rescueCaseId);

    List<ReportCase> findByAdoptionCase_AdoptionCaseId(Integer adoptionCaseId);

    boolean existsByRescueCaseIdAndLostCaseIdAndAdoptionCaseIdAndReportTitle(Integer rescueCaseId, Integer lostCaseId,
            Integer adoptionCaseId, String reportTitle);

    // // 根據 ReportType 查詢
    // List<ReportCase> findByReportType(String reportType);

    // 根據 Member ID 查詢
    // List<ReportCase> findByMemberId(Integer memberId);

    // 根據 Admin ID 查詢 ???
    // List<ReportCase> findByAdminId(Integer adminId);

}
