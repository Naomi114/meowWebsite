package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.com.ispan.domain.pet.DistinctArea;

@Repository
public interface DistinctAreaRepository extends JpaRepository<DistinctArea, Integer> {
    // 您可以在這裡添加自定義的查詢方法，例如：
    // Optional<DistinctArea> findByDistinctAreaName(String distinctAreaName);

    List<DistinctArea> findByCity_CityId(Integer cityId); // 根據城市 ID 查詢區域

}
