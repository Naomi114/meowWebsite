package tw.com.ispan.service.banner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.banner.Banner;
import tw.com.ispan.domain.pet.banner.BannerType;
import tw.com.ispan.dto.pet.BannerDTO;
import tw.com.ispan.repository.pet.BannerRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseRepository;

@Service
@Transactional
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private LostCaseRepository lostCaseRepository;
    @Autowired
    private RescueCaseRepository rescueCaseRepository;
    @Autowired
    private AdoptionCaseRepository adoptionCaseRepository;

    public List<BannerDTO> getAllBanners() {
        List<Banner> banners = bannerRepository.findAll();

        return banners.stream()
                .filter(b -> !b.getIsHidden()) // ✅ 過濾隱藏的 Banner
                .map(this::mapToBannerDTO) // ✅ 轉換為 DTO
                .sorted(Comparator.comparing(BannerDTO::getOnlineDate).reversed()) // ✅ 按時間降序排列
                .collect(Collectors.toList());
    }

    /**
     * ✅ 根據 Banner 類型 (LOST / RESCUE / ADOPTION) 設定對應的 CaseId、CaseTitle 和
     * CasePictures
     */
    private BannerDTO mapToBannerDTO(Banner banner) {
        BannerDTO dto = new BannerDTO();
        dto.setBannerId(banner.getBannerId());
        dto.setBannerType(banner.getBannerType().name());
        dto.setOnlineDate(banner.getOnlineDate());

        // ✅ 根據 Banner 類型處理不同的案件
        switch (banner.getBannerType()) {
            case LOST:
                if (banner.getLostCase() != null) {
                    dto.setLostCaseId(banner.getLostCase().getLostCaseId());
                    dto.setCaseTitle(banner.getLostCase().getCaseTitle());
                    dto.setCasePictures(convertCasePictures(banner.getLostCase().getCasePictures()));
                }
                break;

            case RESCUE:
                if (banner.getRescueCase() != null) {
                    dto.setRescueCaseId(banner.getRescueCase().getRescueCaseId());
                    dto.setCaseTitle(banner.getRescueCase().getCaseTitle());
                    dto.setCasePictures(convertCasePictures(banner.getRescueCase().getCasePictures()));
                }
                break;

            case ADOPTION:
                if (banner.getAdoptionCase() != null) {
                    dto.setAdoptionCaseId(banner.getAdoptionCase().getAdoptionCaseId());
                    dto.setCaseTitle(banner.getAdoptionCase().getCaseTitle());
                    dto.setCasePictures(convertCasePictures(banner.getAdoptionCase().getCasePictures()));
                }
                break;
        }

        return dto;
    }

    /**
     * ✅ 將 `CasePictures` 轉換為前端可用的 URL
     */
    private List<Map<String, String>> convertCasePictures(List<CasePicture> casePictures) {
        // ✅ 取得 API Base URL，確保適應本機與雲端
        String baseURL = System.getenv("VITE_API_BASE_URL") != null
                ? System.getenv("VITE_API_BASE_URL")
                : "http://localhost:8080";

        if (casePictures == null || casePictures.isEmpty()) {
            // ✅ 若無圖片，則提供預設圖片
            Map<String, String> defaultImage = new HashMap<>();
            defaultImage.put("pictureUrl", baseURL + "/images/default.png");
            return Collections.singletonList(defaultImage);
        }

        return casePictures.stream()
                .map(pic -> {
                    String filePath = pic.getPictureUrl().replace("\\", "/"); // 確保 `/` 格式一致
                    filePath = convertBackendPath(filePath, baseURL);

                    Map<String, String> imageMap = new HashMap<>();
                    imageMap.put("pictureUrl", filePath);
                    return imageMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * ✅ 將後端的本機路徑 (`C:/upload/final/...`) 轉換為雲端可讀取的 URL
     */
    private String convertBackendPath(String path, String baseURL) {
        if (path == null || path.isEmpty()) {
            return baseURL + "/images/default.png";
        }

        path = path.replace("\\", "/"); // ✅ 確保所有 `/` 格式統一（Windows & Linux）

        if (path.startsWith("C:/upload/final/")) {
            // ✅ 第一步：將 Windows 本機路徑轉換成 `localhost:8080`
            path = path.replace("C:/upload/final", "http://localhost:8080/upload/final");
        }

        if (path.startsWith("http://localhost:8080")) {
            // ✅ 第二步：將 `localhost:8080` 替換為 `petfinder.duckdns.org`
            path = path.replace("http://localhost:8080", "https://petfinder.duckdns.org");
        }

        if (path.startsWith("/upload/final/")) {
            // ✅ 第三步：如果仍是相對路徑，補上 `BASE_URL`
            path = baseURL + path;
        }

        return path;
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
            case ADOPTION:
                return createAdoptionCaseBanner(adoptioncaseId, bannerType);
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
    private Banner createAdoptionCaseBanner(Integer caseId, BannerType bannerType) {
        if (caseId == null) {
            throw new IllegalArgumentException("AdoptionCase ID 不能為 null");
        }
        AdoptionCase adoptionCase = adoptionCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("AdoptionCase 不存在，ID: " +
                        caseId));

        return saveBanner(null, null, adoptionCase, bannerType);
    }

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

    // ✅ 透過 bannerId 查詢對應的案件
    public Optional<Banner> findCaseByBannerId(Integer bannerId) {
        return bannerRepository.findBannerWithCaseById(bannerId);
    }

    public void deleteBannerByCaseId(Integer caseId, BannerType bannerType) {
        bannerRepository.deleteByLostCase_LostCaseIdAndBannerType(caseId, bannerType);
        bannerRepository.deleteByRescueCase_RescueCaseIdAndBannerType(caseId,
                bannerType);
        bannerRepository.deleteByAdoptionCase_AdoptionCaseIdAndBannerType(caseId,
                bannerType);
    }

}
