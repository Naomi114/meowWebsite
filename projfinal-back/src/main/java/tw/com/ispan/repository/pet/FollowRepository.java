package tw.com.ispan.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

}
