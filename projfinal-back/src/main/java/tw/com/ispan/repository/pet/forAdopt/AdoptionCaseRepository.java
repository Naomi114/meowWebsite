package tw.com.ispan.repository.pet.forAdopt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Species;

@Repository
public interface AdoptionCaseRepository
                extends JpaRepository<AdoptionCase, Integer>, JpaSpecificationExecutor<AdoptionCase> {
        long countByMemberId(Integer memberId);

}
