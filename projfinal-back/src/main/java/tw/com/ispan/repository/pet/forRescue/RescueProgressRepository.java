package tw.com.ispan.repository.pet.forRescue;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.forRescue.RescueProgress;

public interface RescueProgressRepository extends JpaRepository<RescueProgress, Integer> {

	// 根據案件id查詢救援進度
	List<RescueProgress> findByRescueCase_RescueCaseId(Integer rescueCaseId);

	// 根據案件id和進度id查詢救援進度
	Optional<RescueProgress> findByRescueCase_RescueCaseIdAndRescueProgressId(Integer caseId, Integer progressId);

}
