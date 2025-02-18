package tw.com.ispan.repository.pet.forAdopt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.AdoptionCase;

@Repository
public interface AdoptionCaseRepository
                extends JpaRepository<AdoptionCase, Integer>, JpaSpecificationExecutor<AdoptionCase> {
        long countByMemberId(Integer memberId);

        // 查询所有认养案件
        List<AdoptionCase> findAll();

}
