package tw.com.ispan.service.pet.forRescue;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.pet.forRescue.RescueProgress;
import tw.com.ispan.repository.pet.forRescue.RescueProgressRepository;

@Service
public class RescueProgressService {

	@Autowired
	private RescueProgressRepository rescueProgressRepository;

	// 根據案件 ID 取得進度列表
	public List<RescueProgress> getProgressByCaseId(Integer caseId) {
		return rescueProgressRepository.findByRescueCase_RescueCaseId(caseId);
	}

	// 新增進度
	public RescueProgress addProgress(RescueProgress rescueProgress) {
		return rescueProgressRepository.save(rescueProgress);
	}

	// 根據案件 ID 和進度 ID 取得進度
	public Optional<RescueProgress> getProgressByCaseIdAndProgressId(Integer caseId, Integer progressId) {
		return rescueProgressRepository.findByRescueCase_RescueCaseIdAndRescueProgressId(caseId, progressId);
	}

	// 更新進度
	public RescueProgress updateRescueProgress(RescueProgress progress) {
		return rescueProgressRepository.save(progress);
	}

}
