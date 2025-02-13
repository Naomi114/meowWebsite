package tw.com.ispan.repository.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.DistrictArea;

public interface DistrictAreaRepository extends JpaRepository<DistrictArea, Integer> {

    List<DistrictArea> findByCity_CityId(Integer cityId);
}
