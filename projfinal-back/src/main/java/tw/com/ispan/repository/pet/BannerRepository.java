package tw.com.ispan.repository.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.pet.Banner.Banner;
import tw.com.ispan.domain.pet.Banner.BannerType;

public interface BannerRepository extends JpaRepository<Banner, Integer> {

    Optional<Banner> findByLostCase_LostCaseIdAndBannerType(Integer lostCaseId, BannerType bannerType);

    Optional<Banner> findByRescueCase_RescueCaseIdAndBannerType(Integer rescueCaseId, BannerType bannerType);

    Optional<Banner> findByAdoptionCase_AdoptionCaseIdAndBannerType(Integer adoptionCaseId, BannerType bannerType);

    List<Banner> findAll(); // 取得所有 Banner

    List<Banner> findByDueDateBefore(LocalDateTime now);

    void deleteByLostCase_LostCaseIdAndBannerType(Integer caseId, BannerType bannerType);

    // void deleteByRescueCase_RescueCaseIdAndBannerType(Integer caseId, BannerType
    // bannerType);

    // void deleteByAdoptionCase_AdoptionCaseIdAndBannerType(Integer caseId,
    // BannerType bannerType);

}