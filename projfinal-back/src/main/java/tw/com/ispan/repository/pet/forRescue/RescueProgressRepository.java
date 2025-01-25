package tw.com.ispan.repository.pet.forRescue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.pet.forRescue.RescueProgress;

public interface RescueProgressRepository extends JpaRepository<RescueProgress, Integer>{
	
	//根據案件id查詢救援進度
	List<RescueProgress> findByRescueCase_RescueCaseId(Integer rescueCaseId);
}
