package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.pet.LostCase;

public interface LostCaseRepository
        extends JpaRepository<LostCase, Integer>, JpaSpecificationExecutor<LostCase> {
}
