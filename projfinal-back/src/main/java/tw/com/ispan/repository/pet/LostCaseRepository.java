package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.pet.LostCase;

public interface LostCaseRepository
        extends JpaRepository<LostCase, Integer>, LostCaseDAO, JpaSpecificationExecutor<LostCase> {
    List<LostCase> findAllByIsHiddenFalse();

}
