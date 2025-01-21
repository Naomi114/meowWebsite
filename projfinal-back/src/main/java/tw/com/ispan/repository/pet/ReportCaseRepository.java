package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.ReportCase;

@Repository
public interface ReportCaseRepository extends JpaRepository<ReportCase, Integer>, ReportCaseDAO {

    boolean existsByRescueCase_RescueCaseIdAndLostCase_LostCaseIdAndAdoptionCase_AdoptionCaseIdAndReportTitle(
            Integer rescueCaseId, Integer lostCaseId, Integer adoptionCaseId, String reportTitle);

    // // 根據 ReportType 查詢
    // List<ReportCase> findByReportType(String reportType);

}
