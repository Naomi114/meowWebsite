package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.CaseState;

@Repository
public interface CaseStateRepository extends JpaRepository<CaseState, Integer> {

}
