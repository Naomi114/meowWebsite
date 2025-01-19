package tw.com.ispan.service.banner;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.banner.LostBanner;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.banner.LostBannerRepository;

@Service
@Transactional
public class LostBannerService {

    @Autowired
    private LostBannerRepository lostBannerRepository;

    @Autowired
    private LostCaseRepository lostCaseRepository;

    /**
     * 為指定的 LostCase 創建對應的 LostBanner
     */
    public LostBanner createBannerForLostCase(Integer lostCaseId, LocalDateTime onlineDate, LocalDateTime dueDate) {
        LostCase lostCase = lostCaseRepository.findById(lostCaseId)
                .orElseThrow(() -> new EntityNotFoundException("LostCase not found with ID: " + lostCaseId));

        LostBanner lostBanner = new LostBanner();
        lostBanner.setLostCase(lostCase);
        lostBanner.setOnlineDate(onlineDate);
        lostBanner.setDueDate(dueDate);

        return lostBannerRepository.save(lostBanner);
    }

    /**
     * 獲取 LostBanner 根據 LostCase ID
     */
    public Optional<LostBanner> getBannerByLostCaseId(Integer lostCaseId) {
        return lostBannerRepository.findByLostCase_LostCaseId(lostCaseId);
    }

    /**
     * 更新廣告牆的日期
     */
    public LostBanner updateBannerDates(Integer bannerId, LocalDateTime newOnlineDate, LocalDateTime newDueDate) {
        LostBanner lostBanner = lostBannerRepository.findById(bannerId)
                .orElseThrow(() -> new EntityNotFoundException("LostBanner not found with ID: " + bannerId));

        lostBanner.setOnlineDate(newOnlineDate);
        lostBanner.setDueDate(newDueDate);

        return lostBannerRepository.save(lostBanner);
    }

    /**
     * 刪除廣告牆
     */
    public void deleteBanner(Integer bannerId) {
        if (lostBannerRepository.existsById(bannerId)) {
            lostBannerRepository.deleteById(bannerId);
        } else {
            throw new EntityNotFoundException("LostBanner not found with ID: " + bannerId);
        }
    }
}
