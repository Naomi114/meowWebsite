package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.City;

public interface CityRepository  extends JpaRepository< City, Integer>{

}
