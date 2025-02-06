package tw.com.ispan.repository.pet.forAdopt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.pet.forAdopt.AdoptionCaseApply;

public interface AdoptionCaseApplyRepository extends JpaRepository<AdoptionCaseApply, Integer> {
    // 如果需要，你可以在這裡加入自定義的查詢方法

    // 範例：根據 memberId 查找申請紀錄
    public List<AdoptionCaseApply> findByMember_MemberId(Integer memberId);

    // 範例：根據 applicationStatus 查找所有未處理的申請
    List<AdoptionCaseApply> findByApplicationStatus(boolean applicationStatus);
}
