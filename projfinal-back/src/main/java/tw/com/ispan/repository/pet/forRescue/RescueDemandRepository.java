package tw.com.ispan.repository.pet.forRescue;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.forRescue.RescueDemand;

public interface RescueDemandRepository extends JpaRepository<RescueDemand, Integer> {

}
