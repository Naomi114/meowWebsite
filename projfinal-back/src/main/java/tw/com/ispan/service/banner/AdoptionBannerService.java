// package tw.com.ispan.service.banner;

// import java.time.LocalDateTime;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import jakarta.persistence.EntityNotFoundException;
// import tw.com.ispan.domain.pet.AdoptionCase;
// import tw.com.ispan.domain.pet.banner.AdoptionBanner;
// import tw.com.ispan.repository.pet.banner.AdoptionBannerRepository;

// @Service
// @Transactional
// public class AdoptionBannerService {

// @Autowired
// private AdoptionBannerRepository adoptionBannerRepository;
// @Autowired
// private AdoptionCaseRespository adoptionCaseRespository;

// /**
// * 為指定的 AdoptionCase 創建對應的 AdoptionBanner
// */
// public AdoptionBanner createBannerForAdoptionCase(Integer adoptionCaseId,
// LocalDateTime onlineDate,
// LocalDateTime dueDate) {
// AdoptionCase adoptionCase = adoptionCaseRepository.findById(adoptionCaseId)
// .orElseThrow(() -> new EntityNotFoundException("AdoptionCase not found with
// ID: " + adoptionCaseId));

// AdoptionBanner adoptionBanner = new AdoptionBanner();
// adoptionBanner.setAdoptionCase(adoptionCase);
// adoptionBanner.setOnlineDate(onlineDate);
// adoptionBanner.setDueDate(dueDate);

// return adoptionBannerRepository.save(adoptionBanner);
// }

// /**
// * 獲取 AdoptionBanner 根據 AdoptionCase ID
// */
// public Optional<AdoptionBanner> getBannerByAdoptionCaseId(Integer
// adoptionCaseId) {
// return
// adoptionBannerRepository.findByAdoptionCase_AdoptionCaseId(adoptionCaseId);
// }

// /**
// * 更新廣告牆的日期
// */
// public AdoptionBanner updateBannerDates(Integer bannerId, LocalDateTime
// newOnlineDate, LocalDateTime newDueDate) {
// AdoptionBanner adoptionBanner = adoptionBannerRepository.findById(bannerId)
// .orElseThrow(() -> new EntityNotFoundException("AdoptionBanner not found with
// ID: " + bannerId));

// adoptionBanner.setOnlineDate(newOnlineDate);
// adoptionBanner.setDueDate(newDueDate);

// return adoptionBannerRepository.save(adoptionBanner);
// }

// /**
// * 刪除廣告牆
// */
// public void deleteBanner(Integer bannerId) {
// if (adoptionBannerRepository.existsById(bannerId)) {
// adoptionBannerRepository.deleteById(bannerId);
// } else {
// throw new EntityNotFoundException("AdoptionBanner not found with ID: " +
// bannerId);
// }
// }
// }