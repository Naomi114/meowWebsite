package tw.com.ispan.repository.pet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.Species;

public interface SpeciesRepository extends JpaRepository<Species, Integer> {

}
