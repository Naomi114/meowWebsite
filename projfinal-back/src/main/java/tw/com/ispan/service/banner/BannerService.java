package tw.com.ispan.service.banner;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.banner.Banner;
import tw.com.ispan.domain.pet.banner.BannerType;
import tw.com.ispan.repository.pet.BannerRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;

@Service
@Transactional
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private LostCaseRepository lostCaseRepository;
    @Autowired
    private RescueCaseRepository rescueCaseRepository;
    // @Autowired
    // private AdoptionCaseRepository adoptionCaseRepository;

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll(); // 從資料庫獲取所有 Banner
    }

    /**
     * 根據 Case ID 創建 Banner
     */
    public Banner createBanner(Integer lostcaseId, Integer rescuecaseId, Integer adoptioncaseId,
            BannerType bannerType) {

        // 依據 BannerType 類型，尋找對應的 Case
        switch (bannerType) {
            case LOST:
                return createLostCaseBanner(lostcaseId, bannerType);
            case RESCUE:
                return createRescueCaseBanner(rescuecaseId, bannerType);
            // case ADOPTION:
            // return createAdoptionCaseBanner(adoptioncaseId, bannerType);
            default:
                throw new IllegalArgumentException("未知的 BannerType: " + bannerType);
        }
    }

    /**
     * 創建遺失案件 (LostCase) 的 Banner
     */
    private Banner createLostCaseBanner(Integer caseId, BannerType bannerType) {
        if (caseId == null) {
            throw new IllegalArgumentException("LostCase ID 不能為 null");
        }
        LostCase lostCase = lostCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("LostCase 不存在，ID: " + caseId));

        return saveBanner(lostCase, null, null, bannerType);
    }

    /**
     * 創建救援案件 (RescueCase) 的 Banner
     */
    private Banner createRescueCaseBanner(Integer caseId, BannerType bannerType) {
        if (caseId == null) {
            throw new IllegalArgumentException("RescueCase ID 不能為 null");
        }
        RescueCase rescueCase = rescueCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("RescueCase 不存在，ID: " + caseId));

        return saveBanner(null, rescueCase, null, bannerType);
    }

    /**
     * 創建領養案件 (AdoptionCase) 的 Banner
     */
    // private Banner createAdoptionCaseBanner(Integer caseId, BannerType
    // bannerType) {
    // if (caseId == null) {
    // throw new IllegalArgumentException("AdoptionCase ID 不能為 null");
    // }
    // AdoptionCase adoptionCase = adoptionCaseRepository.findById(caseId)
    // .orElseThrow(() -> new IllegalArgumentException("AdoptionCase 不存在，ID: " +
    // caseId));

    // return saveBanner(null, null, adoptionCase, bannerType);
    // }

    /**
     * 儲存 Banner 通用方法
     */
    private Banner saveBanner(LostCase lostCase, RescueCase rescueCase, AdoptionCase adoptionCase,
            BannerType bannerType) {
        Banner banner = new Banner();
        banner.setLostCase(lostCase);
        banner.setRescueCase(rescueCase);
        banner.setAdoptionCase(adoptionCase);
        banner.setBannerType(bannerType);
        banner.setOnlineDate(LocalDateTime.now());
        banner.setDueDate(LocalDateTime.now().plusDays(30)); // 設定 30 天有效期
        banner.setIsHidden(false); // 初始時不隱藏

        return bannerRepository.save(banner);
    }

    /**
     * 隱藏已過期的 Banner
     */
    public void hideExpiredBanners() {
        List<Banner> expiredBanners = bannerRepository.findByDueDateBefore(LocalDateTime.now());
        for (Banner banner : expiredBanners) {
            banner.setIsHidden(true); // 設置為隱藏
            bannerRepository.save(banner);
        }
    }

    /**
     * 根據 Banner 類型查詢對應的 Banners
     *
     * @param bannerType Banner 類型（LOST, ADOPTION, RESCUE）
     * @return 符合條件的 Banner 列表
     */
    public List<Banner> findByBannerType(BannerType bannerType) {
        return bannerRepository.findByBannerType(bannerType);
    }

    public void deleteBannerByCaseId(Integer caseId, BannerType bannerType) {
        bannerRepository.deleteByLostCase_LostCaseIdAndBannerType(caseId, bannerType);
        bannerRepository.deleteByRescueCase_RescueCaseIdAndBannerType(caseId,
                bannerType);
        bannerRepository.deleteByAdoptionCase_AdoptionCaseIdAndBannerType(caseId,
                bannerType);
    }

}
