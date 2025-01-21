package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import tw.com.ispan.domain.pet.LostCase;

public interface LostCaseRepository
        extends JpaRepository<LostCase, Integer>, LostCaseDAO, JpaSpecificationExecutor<LostCase> {
    // 在 LostCaseRepository 中加入
    @Query("SELECT lc FROM LostCase lc WHERE lc.isHidden = false")
    List<LostCase> findVisibleLostCases();
}
