package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.pet.CaseView;

public interface CaseViewRepository extends JpaRepository<CaseView, Integer> {

        // Rescue 案件前 10名
        @Query("SELECT c.rescueCase.rescueCaseId, c.rescueCase.caseTitle, COUNT(c) " +
                        "FROM CaseView c WHERE c.rescueCase IS NOT NULL " +
                        "GROUP BY c.rescueCase.rescueCaseId, c.rescueCase.caseTitle " +
                        "ORDER BY COUNT(c) DESC")
        List<Object[]> findTopViewedRescueCases();

        // Rescue 案件後 10 名
        @Query("SELECT c.rescueCase.rescueCaseId, c.rescueCase.caseTitle, COUNT(c) " +
                        "FROM CaseView c WHERE c.rescueCase IS NOT NULL " +
                        "GROUP BY c.rescueCase.rescueCaseId, c.rescueCase.caseTitle " +
                        "ORDER BY COUNT(c) ASC")
        List<Object[]> findBottomViewedRescueCases();

        // Lost 案件前 10名
        @Query("SELECT c.lostCase.lostCaseId, c.lostCase.caseTitle, COUNT(c) " +
                        "FROM CaseView c WHERE c.lostCase IS NOT NULL " +
                        "GROUP BY c.lostCase.lostCaseId, c.lostCase.caseTitle " +
                        "ORDER BY COUNT(c) ASC")
        List<Object[]> findTopViewedLostCases();

        // Lost 案件後 10名
        @Query("SELECT c.rescueCase.rescueCaseId, c.rescueCase.caseTitle, COUNT(c) " +
                        "FROM CaseView c WHERE c.rescueCase IS NOT NULL " +
                        "GROUP BY c.rescueCase.rescueCaseId, c.rescueCase.caseTitle " +
                        "ORDER BY COUNT(c) ASC")
        List<Object[]> findBottomViewedLostCases();

        // Adoption 案件前 10 名
        @Query("SELECT c.adoptionCase.id, c.adoptionCase.caseTitle, COUNT(c) " +
                        "FROM CaseView c WHERE c.adoptionCase IS NOT NULL " +
                        "GROUP BY c.adoptionCase.id ORDER BY COUNT(c) DESC LIMIT 10")
        List<Object[]> findTopViewedAdoptionCases();

        // Adoption 案件後 10 名
        @Query("SELECT c.adoptionCase.id, c.adoptionCase.caseTitle, COUNT(c) " +
                        "FROM CaseView c WHERE c.adoptionCase IS NOT NULL " +
                        "GROUP BY c.adoptionCase.id ORDER BY COUNT(c) ASC LIMIT 10")
        List<Object[]> findBottomViewedAdoptionCases();

        // 分析頁面獲得單一案件在特定時間內的瀏覽人次，讓查詢的 viewTime 只顯示日期 (YYYY-MM-DD)，並且按日期統計瀏覽人數
        @Query("SELECT FORMAT(cv.viewTime, 'yyyy-MM-dd'), COUNT(cv) " +
                        "FROM CaseView cv " +
                        "WHERE (cv.rescueCase.rescueCaseId = :caseId AND :caseType = 'rescue') " +
                        "   OR (cv.lostCase.lostCaseId = :caseId AND :caseType = 'lost') " +
                        "   OR (cv.adoptionCase.adoptionCaseId = :caseId AND :caseType = 'adoption') " +
                        "GROUP BY FORMAT(cv.viewTime, 'yyyy-MM-dd') " +
                        "ORDER BY FORMAT(cv.viewTime, 'yyyy-MM-dd')")
        List<Object[]> findCaseViewTrend(@Param("caseType") String caseType, @Param("caseId") Integer caseId);

        // 幫助計算特定案件被瀏覽的次數
        @Query("SELECT COUNT(c) FROM CaseView c WHERE c.rescueCase.rescueCaseId =:caseId")
        int countByRescueCaseId(@Param("caseId") Integer caseId);

        // 跟著案件刪除一起被刪掉
        @Modifying
        @Query("DELETE FROM CaseView cv WHERE cv.rescueCase.rescueCaseId = :rescueCaseId")
        void deleteByRescueCaseId(@Param("rescueCaseId") Integer rescueCaseId);
}
