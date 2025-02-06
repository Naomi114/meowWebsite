package tw.com.ispan.service.pet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.forAdopt.AdoptionCaseApply;
import tw.com.ispan.dto.AdoptionCaseApplyDTO;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseApplyRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseRepository;

@Service
public class AdoptionCaseApplyService {

    @Autowired
    private AdoptionCaseApplyRepository adoptionCaseApplyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdoptionCaseRepository adoptionCaseRepository;

    // 新增申請邏輯
    public AdoptionCaseApply createAdoptionCaseApply(AdoptionCaseApplyDTO adoptionCaseApplyDTO) {
        // 驗證傳入的資料
        if (adoptionCaseApplyDTO.getIntroduction() == null || adoptionCaseApplyDTO.getIntroduction().isEmpty()) {
            throw new IllegalArgumentException("Introduction cannot be null or empty");
        }

        // 查找 Member
        Member member = memberRepository.findById(adoptionCaseApplyDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 查找單一的 AdoptionCase，這裡改為 findById
        AdoptionCase adoptionCase = adoptionCaseRepository.findById(adoptionCaseApplyDTO.getAdoptionCaseId())
                .orElseThrow(() -> new IllegalArgumentException("AdoptionCase not found"));

        // 創建新的申請
        AdoptionCaseApply newApply = new AdoptionCaseApply();
        newApply.setIntroduction(adoptionCaseApplyDTO.getIntroduction());
        newApply.setApplicationStatus(adoptionCaseApplyDTO.isApplicationStatus());
        newApply.setMember(member);
        newApply.setAdoptionCase(Set.of(adoptionCase)); // 將單一 AdoptionCase 包裝成 Set

        // 儲存並返回
        return adoptionCaseApplyRepository.save(newApply);
    }
}
