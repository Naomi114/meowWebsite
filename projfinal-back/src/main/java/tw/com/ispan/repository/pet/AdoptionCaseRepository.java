package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.pet.AdoptionCase;

public interface AdoptionCaseRepository  extends JpaRepository<AdoptionCase, Integer>, JpaSpecificationExecutor<AdoptionCase> {

}
