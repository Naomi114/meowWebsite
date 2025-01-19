package tw.com.ispan.repository.pet.banner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.banner.LostBanner;

public interface LostBannerRepository extends JpaRepository<LostBanner, Integer> {

    // 查詢 LostBanner 根據 LostCase ID
    Optional<LostBanner> findByLostCase_LostCaseId(Integer lostCaseId);

    // 查詢 LostBanner 根據 LostCase
    Optional<LostBanner> findByLostCase(LostCase lostCase);

}