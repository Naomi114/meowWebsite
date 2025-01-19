package tw.com.ispan.service.banner;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.banner.RescueBanner;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.banner.RescueBannerRepository;

@Service
@Transactional
public class RescueBannerService {

    @Autowired
    private RescueBannerRepository rescueBannerRepository;
    @Autowired
    private RescueCaseRepository rescueCaseRepository;

    /**
     * 為指定的 RescueCase 創建對應的 RescueBanner
     */
    public RescueBanner createBannerForRescueCase(Integer lostCaseId, LocalDateTime onlineDate, LocalDateTime dueDate) {
        RescueCase lostCase = rescueCaseRepository.findById(lostCaseId)
                .orElseThrow(() -> new EntityNotFoundException("RescueCase not found with ID: " + lostCaseId));

        RescueBanner lostBanner = new RescueBanner();
        lostBanner.setRescueCase(lostCase);
        lostBanner.setOnlineDate(onlineDate);
        lostBanner.setDueDate(dueDate);

        return rescueBannerRepository.save(lostBanner);
    }

    /**
     * 獲取 RescueBanner 根據 RescueCase ID
     */
    public Optional<RescueBanner> getBannerByRescueCaseId(Integer lostCaseId) {
        return rescueBannerRepository.findByRescueCase_RescueCaseId(lostCaseId);
    }

    /**
     * 更新廣告牆的日期
     */
    public RescueBanner updateBannerDates(Integer bannerId, LocalDateTime newOnlineDate, LocalDateTime newDueDate) {
        RescueBanner lostBanner = rescueBannerRepository.findById(bannerId)
                .orElseThrow(() -> new EntityNotFoundException("RescueBanner not found with ID: " + bannerId));

        lostBanner.setOnlineDate(newOnlineDate);
        lostBanner.setDueDate(newDueDate);

        return rescueBannerRepository.save(lostBanner);
    }

    /**
     * 刪除廣告牆
     */
    public void deleteBanner(Integer bannerId) {
        if (rescueBannerRepository.existsById(bannerId)) {
            rescueBannerRepository.deleteById(bannerId);
        } else {
            throw new EntityNotFoundException("RescueBanner not found with ID: " + bannerId);
        }
    }
}
