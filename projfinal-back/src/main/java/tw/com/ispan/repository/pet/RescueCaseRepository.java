package tw.com.ispan.repository.pet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.pet.RescueCase;

public interface RescueCaseRepository extends JpaRepository<RescueCase, Integer>, JpaSpecificationExecutor<RescueCase> {

	// 返回某會員建立的救援案件
	List<RescueCase> findByMemberId(Integer memberId);

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
			@Param("isLost") Boolean isLost, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	// 查詢瀏覽人次最多的前 10 筆案件(給管理員後台分析使用)
	List<RescueCase> findTop10ByOrderByViewCountDesc();

	// 獲取後 10 名（最少瀏覽人次）
	List<RescueCase> findTop10ByOrderByViewCountAsc();

	// 此方法用來統計每個縣市的案件數量，返回一個 List<Object[]>，每個 Object[] 內包含兩個值： c[0] = 縣市名稱 c[1] =
	// 該縣市的案件數量
	// JOIN r.city c：將 RescueCase 與 City 表做 關聯查詢 COUNT(r)：統計每個縣市 (c.city) 的案件數量
	// GROUP BY c.city：依據縣市名稱分組統計
	@Query("SELECT c.city, COUNT(r) FROM RescueCase r JOIN r.city c GROUP BY c.city")
	List<Object[]> countCasesByCity();

	// 這個方法會根據物種（狗或貓）計算案件數量 意思是 WHERE species.species = :species
	long countBySpecies_Species(String species);

	// 用戶進入案件頁面時更新某案件的瀏覽次數
	@Modifying
	@Query("UPDATE RescueCase r SET r.viewCount = :viewCount WHERE r.rescueCaseId = :caseId")
	void updateViewCount(@Param("caseId") Integer caseId, @Param("viewCount") int viewCount);

	// 給資料初始化程式使用
	List<RescueCase> findByRescueCaseIdBetween(int start, int end);

	@Query("SELECT rc FROM RescueCase rc JOIN FETCH rc.member WHERE rc.rescueCaseId = :id")
	Optional<RescueCase> findByIdWithMember(@Param("id") Integer id);

}
