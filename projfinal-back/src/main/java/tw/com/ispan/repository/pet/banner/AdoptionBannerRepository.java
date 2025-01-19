// package tw.com.ispan.repository.pet.banner;

// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;

// import tw.com.ispan.domain.pet.AdoptionCase;
// import tw.com.ispan.domain.pet.banner.AdoptionBanner;

// public interface AdoptionBannerRepository extends
// JpaRepository<AdoptionBanner, Integer> {

// // 查詢 AdoptionBanner 根據 AdoptionCase ID
// Optional<AdoptionBanner> findByAdoptionCase_AdoptionCaseId(Integer
// adoptionCaseId);

// // 查詢 AdoptionBanner 根據 AdoptionCase
// Optional<AdoptionBanner> findByAdoptionCase(AdoptionCase adoptionCase);
// }