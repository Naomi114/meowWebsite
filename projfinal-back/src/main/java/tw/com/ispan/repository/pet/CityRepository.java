package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.pet.City;

public interface CityRepository extends JpaRepository<City, Integer> {

	// 由於僅提供內部資料初始化使用，因此不怕SQL injection
	@Query("SELECT c.id FROM City c WHERE c.id BETWEEN :start AND :end")
	List<Integer> findCityIdsInRange(@Param("start") int start, @Param("end") int end);
}
