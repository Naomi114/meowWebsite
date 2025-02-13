package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer>{

}
