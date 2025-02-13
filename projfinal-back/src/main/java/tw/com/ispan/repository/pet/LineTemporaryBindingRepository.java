package tw.com.ispan.repository.pet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.LineTemporaryBinding;

@Repository
public interface LineTemporaryBindingRepository extends JpaRepository<LineTemporaryBinding, Integer> {
    
	Optional<LineTemporaryBinding> findByBindingToken(String token);
}