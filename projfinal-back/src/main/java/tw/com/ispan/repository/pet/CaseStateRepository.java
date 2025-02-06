package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.CaseState;

public interface CaseStateRepository extends JpaRepository<CaseState, Integer>{

}
