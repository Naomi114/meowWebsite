package tw.com.ispan.repository.pet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.CasePicture;

public interface CasePictureRepository extends JpaRepository<CasePicture, Integer> {
    // Optional<CasePicture> findByLostCaseId(Integer lostCaseId);

    // Optional<CasePicture> findByRescueCaseId(Integer rescueCaseId);

    // Optional<CasePicture> findByAdoptionCaseId(Integer adoptionCaseId);
}
