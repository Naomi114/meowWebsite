package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.DistinctArea;

public interface DistinctAreaRepository extends JpaRepository<DistinctArea, Integer> {

}
