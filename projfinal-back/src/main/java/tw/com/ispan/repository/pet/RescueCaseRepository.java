package tw.com.ispan.repository.pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.pet.RescueCase;

public interface RescueCaseRepository extends JpaRepository<RescueCase, Integer>, JpaSpecificationExecutor<RescueCase> {

	@EntityGraph(attributePaths = { "species" })
	@Query("SELECT r FROM RescueCase r")
	List<RescueCase> findAllCases(Pageable pageable);

	@Query("SELECT r FROM RescueCase r WHERE " +
		    " (:caseState IS NULL OR COALESCE(:caseState, NULL) IS NOT NULL AND r.caseState.id IN :caseState) "
		    + "AND (:city IS NULL OR r.city.cityId = :city) "
		    + "AND (:district IS NULL OR r.districtArea.districtAreaId = :district) "
		    + "AND (:species IS NULL OR COALESCE(:species, NULL) IS NOT NULL AND r.species.speciesId IN :species) "
		    + "AND (:breedId IS NULL OR r.breed.breedId = :breedId) "
		    + "AND (:furColors IS NULL OR COALESCE(:furColors, NULL) IS NOT NULL AND r.furColor.furColorId IN :furColors) "
		    + "AND (:isLost IS NULL OR r.suspLost = :isLost) "
		    + "AND (:startDate IS NULL OR r.publicationTime >= CAST(:startDate AS timestamp)) "
		    + "AND (:endDate IS NULL OR r.publicationTime <= CAST(:endDate AS timestamp))")
		List<RescueCase> findCasesWithFilters(
		    @Param("caseState") List<Integer> caseState, @Param("city") Integer city,
		    @Param("district") Integer district, @Param("species") List<Integer> species,
		    @Param("breedId") Integer breedId, @Param("furColors") List<Integer> furColors,
		    @Param("isLost") Boolean isLost, @Param("startDate") LocalDate  startDate,
		    @Param("endDate") LocalDate  endDate);
}
