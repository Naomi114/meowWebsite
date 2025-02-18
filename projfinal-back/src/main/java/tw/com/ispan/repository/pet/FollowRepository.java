package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Follow;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
	
	//根據會員id找尋其所有追蹤的案件
	@Query("SELECT f FROM Follow f WHERE f.member.memberId = :memberId")
	List<Follow> findByMemberId(@Param("memberId") Integer memberId);
	
	//Spring Data JPA 提供的一種簡化資料庫查詢的方式，基於方法名稱自動生成對應的查詢語句
	//對應於 Follow 實體中的關聯屬性 rescueCase 和 member。 JPA 會自動根據這些屬性映射到資料庫中的外鍵欄位（rescue_case_id 和 member_id）
	boolean existsByRescueCaseAndMember(RescueCase rescueCase, Member member);
    boolean existsByLostCaseAndMember(LostCase lostCase,  Member member);
    boolean existsByAdoptionCaseAndMember(AdoptionCase adoptionCase, Member member);   
    
    //刪除特定memberid+caseid
    @Transactional
    void deleteByRescueCaseAndMember(RescueCase rescueCase, Member member);
    @Transactional
    void deleteByLostCaseAndMember(LostCase lostCase, Member member);
    @Transactional
    void deleteByAdoptionCaseAndMember(AdoptionCase adoptionCase, Member member);
    
    
    //計算某 RescueCase 被追蹤的總數
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.rescueCase.rescueCaseId = :caseId")
    int countByRescueCaseId(Integer caseId);
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.lostCase.lostCaseId = :caseId")
    int countByLostCaseId(Integer caseId);
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.adoptionCase.adoptionCaseId = :caseId")
    int countByAdoptionCaseId(Integer caseId);
    
    //用於發送通知給有追蹤特定案件的會員(依caseId查出會員id清單)
    @Query("SELECT f.member.memberId FROM Follow f WHERE f.rescueCase.rescueCaseId = :caseId")
    List<Integer> findMemberIdsByRescueCaseId(@Param("caseId") Integer caseId);
    
}
