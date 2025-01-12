package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.CasePicture;

public interface CasePictureRepository  extends JpaRepository<CasePicture, Integer> {

}
