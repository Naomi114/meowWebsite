package tw.com.ispan.service.pet;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Follow;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.FollowRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseRepository;

@Service
@Transactional
public class FollowService {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RescueCaseRepository rescueCaseRepository;
	@Autowired
	private FollowRepository followRepository;
	@Autowired
	private LostCaseRepository lostCaseRepository;
	@Autowired
	private AdoptionCaseRepository adoptionCaseRepository;

	// 新增追蹤
	public Follow addFollow(Integer memberId, Integer caseId, String caseType) {
		Follow follow = new Follow();

		// 當按下follow要能區分使用者是對rescue, lost還是adoption
		// case，因為相同caseId在這三表中都有，要查對表-------------------------------

		switch (caseType.toLowerCase()) {
		case "rescue":
			RescueCase rescueCase = rescueCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("案件ID" + caseId + "的RescueCase 不存在"));
			follow.setRescueCase(rescueCase);
			break;

		case "lost":
			LostCase lostCase = lostCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("案件ID" + caseId + "LostCase 不存在"));
			follow.setLostCase(lostCase);
			break;

		case "adoption":
			AdoptionCase adoptionCase = adoptionCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("案件ID" + caseId + "AdoptionCase 不存在"));
			follow.setAdoptionCase(adoptionCase);
			break;

		default:
			throw new IllegalArgumentException("未知的案件類型：" + caseType);
		}

		// 設置會員
		follow.setMember(memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("會員不存在")));

		// 設置時間
		follow.setFollowDate(LocalDateTime.now());

		Follow saveFollow = followRepository.save(follow); // 返回物件會被加進followId流水號
		return saveFollow;

		// 這邊應該要同時更新resuceCase裡面的數量??
	}

	// 用於驗證某類型案件表中是否存在此案件id----------------------------------------------------------
	public boolean caseExists(Integer caseId, String caseType) {
		switch (caseType.toLowerCase()) {
		case "rescue":
			return rescueCaseRepository.existsById(caseId);
		case "lost":
			return lostCaseRepository.existsById(caseId);
		case "adoption":
			return adoptionCaseRepository.existsById(caseId);
		default:
			return false;
		}
	}

	// 確認follow表中是否已經存在某caseType和caseId加上某memberId的組合，避免重複添加資料
	public boolean checkIfFollowExists(Integer memberId, Integer caseId, String caseType) {

		// 查找會員
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("Member 不存在: " + memberId));

		// 根據案件類型查詢對應的記錄
		switch (caseType.toLowerCase()) {
		case "rescue":
			RescueCase rescueCase = rescueCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("RescueCase 不存在: " + caseId));
			return followRepository.existsByRescueCaseAndMember(rescueCase, member);

		case "lost":
			LostCase lostCase = lostCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("LostCase 不存在: " + caseId));
			return followRepository.existsByLostCaseAndMember(lostCase, member);

		case "adoption":
			AdoptionCase adoptionCase = adoptionCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("AdoptionCase 不存在: " + caseId));
			return followRepository.existsByAdoptionCaseAndMember(adoptionCase, member);

		default:
			throw new IllegalArgumentException("未知的案件類型：" + caseType);
		}
	}

	// 刪除特定follow表中資料
	public void removeFollow(Integer memberId, Integer caseId, String caseType) {
		// 查找會員
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("Member 不存在: " + memberId));

		// 根據案件類型查詢對應的記錄
		switch (caseType.toLowerCase()) {
		case "rescue":
			RescueCase rescueCase = rescueCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("RescueCase 不存在: " + caseId));
			followRepository.deleteByRescueCaseAndMember(rescueCase, member);
			break;

		case "lost":
			LostCase lostCase = lostCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("LostCase 不存在: " + caseId));
			followRepository.deleteByLostCaseAndMember(lostCase, member);
			break;

		case "adoption":
			AdoptionCase adoptionCase = adoptionCaseRepository.findById(caseId)
					.orElseThrow(() -> new IllegalArgumentException("AdoptionCase 不存在: " + caseId));
			followRepository.deleteByAdoptionCaseAndMember(adoptionCase, member);
			break;

		default:
			throw new IllegalArgumentException("未知的案件類型：" + caseType);
		}
	}

	// 當有人追蹤某案件時，更新該案件的的follow總數欄位
	@Transactional
	public int updateFollowCount(Integer caseId, String caseType) {
	    int count = 0;

	    switch (caseType) {
	        case "rescue":
	            count = followRepository.countByRescueCaseId(caseId);
	            Optional<RescueCase> rescueCaseOpt = rescueCaseRepository.findById(caseId);
	            if (rescueCaseOpt.isPresent()) {
	                RescueCase rescueCase = rescueCaseOpt.get();
	                rescueCase.setFollow(count);
	                rescueCaseRepository.save(rescueCase);
	            }
	            break;

	        case "lost":
	            count = followRepository.countByLostCaseId(caseId);
	            Optional<LostCase> lostCaseOpt = lostCaseRepository.findById(caseId);
	            if (lostCaseOpt.isPresent()) {
	                LostCase lostCase = lostCaseOpt.get();
	                lostCase.setFollow(count);
	                lostCaseRepository.save(lostCase);
	            }
	            break;

	        case "adoption":
	            count = followRepository.countByAdoptionCaseId(caseId);
	            Optional<AdoptionCase> adoptionCaseOpt = adoptionCaseRepository.findById(caseId);
	            if (adoptionCaseOpt.isPresent()) {
	                AdoptionCase adoptionCase = adoptionCaseOpt.get();
	                adoptionCase.setFollow(count);
	                adoptionCaseRepository.save(adoptionCase);
	            }
	            break;

	        default:
	            throw new IllegalArgumentException("無效的案件類型：" + caseType);
	    }

	    return count;
	}
	
	
	public List<Map<String, Object>> getFollowedCasesByMember(Integer memberId) {
	    List<Follow> follows = followRepository.findByMemberId(memberId);

	    // 將追蹤案件轉換成前端需要的格式
	    return follows.stream().map(follow -> {
	        Map<String, Object> caseMap = new HashMap<>();
	        caseMap.put("followId", follow.getFollowId());
	        caseMap.put("followDate", follow.getFollowDate());
	        
	        // 判斷案件類型
	        if (follow.getRescueCase() != null) {
	            caseMap.put("title", follow.getRescueCase().getCaseTitle());
	            caseMap.put("caseType", "rescue");
	            caseMap.put("caseId", follow.getRescueCase().getRescueCaseId());
	        } else if (follow.getLostCase() != null) {
	            caseMap.put("title", follow.getLostCase().getCaseTitle());
	            caseMap.put("caseType", "lost");
	            caseMap.put("caseId", follow.getLostCase().getLostCaseId());
	        } else if (follow.getAdoptionCase() != null) {
	            caseMap.put("title", follow.getAdoptionCase().getCaseTitle());
	            caseMap.put("caseType", "adoption");
	            caseMap.put("caseId", follow.getAdoptionCase().getAdoptionCaseId());
	        }
	        return caseMap;
	    }).collect(Collectors.toList());
	}
	
}
