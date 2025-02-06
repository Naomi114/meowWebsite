package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.DistrictArea;

@Repository
public interface DistrictAreaRepository extends JpaRepository<DistrictArea, Integer> {

    List<DistrictArea> findByCity_CityId(Integer cityId);

}
