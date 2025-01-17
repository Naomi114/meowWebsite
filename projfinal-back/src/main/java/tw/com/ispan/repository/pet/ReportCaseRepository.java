// package tw.com.ispan.repository.pet;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import tw.com.ispan.domain.pet.ReportCase;

// @Repository
// public interface ReportCaseRepository extends JpaRepository<ReportCase,
// Integer> {

// // // 根據 ReportType 查詢
// // List<ReportCase> findByReportType(String reportType);

// // 根據 Member ID 查詢
// List<ReportCase> findByMemberId(Integer memberId);

// // 根據 Case ID 查詢
// List<ReportCase> findByLostCaseId(Integer lostCaseId);

// List<ReportCase> findByRescueCaseId(Integer rescueCaseId);

// // List<ReportCase> findByAdoptionCaseId(Integer adoptionCaseId);
// }
