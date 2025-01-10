package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.Distint;

public interface DistintRepository extends JpaRepository<Distint, Integer> {

}
