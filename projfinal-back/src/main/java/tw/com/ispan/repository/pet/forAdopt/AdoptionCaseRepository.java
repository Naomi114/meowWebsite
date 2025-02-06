package tw.com.ispan.repository.pet.forAdopt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.pet.AdoptionCase;

public interface AdoptionCaseRepository extends JpaRepository<AdoptionCase, Integer> {

@Query("SELECT ac FROM AdoptionCase ac WHERE " +
"(:cityId IS NULL OR ac.city.cityId = :cityId) AND " +
"(:districtAreaId IS NULL OR ac.districtArea.districtAreaId = :districtAreaId) AND " +
"(:caseStateId IS NULL OR ac.caseState.caseStateId = :caseStateId) AND " +
"(:speciesId IS NULL OR ac.species.speciesId = :speciesId) AND " +
"(:gender IS NULL OR ac.gender LIKE %:gender%)")
    List<AdoptionCase> searchAdoptionCases(
            @Param("cityId") Long cityId,
            @Param("districtAreaId") Long districtAreaId,
            @Param("caseStateId") Long caseStateId,
            @Param("speciesId") Long speciesId,
            @Param("gender") String gender);
}
