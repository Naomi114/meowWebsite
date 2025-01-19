package tw.com.ispan.repository.pet.banner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.banner.RescueBanner;

public interface RescueBannerRepository extends JpaRepository<RescueBanner, Integer> {

    // 查詢 RescueBanner 根據 RescueCase ID
    Optional<RescueBanner> findByRescueCase_RescueCaseId(Integer rescueCaseId);

    // 查詢 RescueBanner 根據 RescueCase
    Optional<RescueBanner> findByRescueCase(RescueCase rescueCase);
}