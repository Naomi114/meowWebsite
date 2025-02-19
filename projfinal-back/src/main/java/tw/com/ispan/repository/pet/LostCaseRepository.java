package tw.com.ispan.repository.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.LostCase;

@Repository
public interface LostCaseRepository
                extends JpaRepository<LostCase, Integer>, JpaSpecificationExecutor<LostCase> {

        List<LostCase> findByMemberId(Integer memberId);

        List<LostCase> findAll(Sort sort);

        @Query("SELECT l FROM LostCase l LEFT JOIN FETCH l.member WHERE l.lostCaseId = :lostCaseId")
        Optional<LostCase> findByIdWithMember(@Param("lostCaseId") Integer lostCaseId);
}
