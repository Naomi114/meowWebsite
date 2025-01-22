package tw.com.ispan.service.pet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.Banner.Banner;
import tw.com.ispan.domain.pet.Banner.BannerType;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BannerRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistinctAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.service.banner.BannerService;

@Service
@Transactional
public class LostCaseService {
    @Autowired
    private LostCaseRepository lostCaseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private FurColorRepository furColorRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistinctAreaRepository distinctAreaRepository;

    @Autowired
    private CaseStateRepository caseStateRepository;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 查詢所有 LostCase，支援模糊查詢
     */
    @Transactional(readOnly = true)
    public List<LostCase> searchLostCases(JSONObject param) {
        return lostCaseRepository.findAll((Specification<LostCase>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 模糊查詢 caseTitle
            if (param.has("caseTitle") && !param.getString("caseTitle").isEmpty()) {
                String likePattern = "%" + param.getString("caseTitle") + "%";
                predicates.add(criteriaBuilder.like(root.get("caseTitle"), likePattern));
            }

            // 根據 speciesId 查詢
            if (param.has("speciesId")) {
                predicates.add(criteriaBuilder.equal(root.get("species").get("speciesId"), param.getInt("speciesId")));
            }

            // 根據 breedId 查詢
            if (param.has("breedId")) {
                predicates.add(criteriaBuilder.equal(root.get("breed").get("breedId"), param.getInt("breedId")));
            }

            // 根據 furColorId 查詢
            if (param.has("furColorId")) {
                predicates
                        .add(criteriaBuilder.equal(root.get("furColor").get("furColorId"), param.getInt("furColorId")));
            }

            // 根據 cityId 查詢
            if (param.has("cityId")) {
                predicates.add(criteriaBuilder.equal(root.get("city").get("cityId"), param.getInt("cityId")));
            }

            // 根據 distinctAreaId 查詢
            if (param.has("distinctAreaId")) {
                predicates.add(criteriaBuilder.equal(root.get("distinctArea").get("distinctAreaId"),
                        param.getInt("distinctAreaId")));
            }

            // 根據案件狀態 caseStateId 查詢
            if (param.has("caseStateId")) {
                predicates.add(
                        criteriaBuilder.equal(root.get("caseState").get("caseStateId"), param.getInt("caseStateId")));
            }

            // 查詢未隱藏的案件
            predicates.add(criteriaBuilder.equal(root.get("isHidden"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    /**
     * 創建 LostCase 並自動創建對應的 Banner
     */
    public LostCase create(JSONObject param) {
        LostCase lostCase = new LostCase();
        lostCase.setCaseTitle(param.getString("caseTitle"));
        lostCase.setMember(memberRepository.findById(param.getInt("memberId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 memberId")));
        lostCase.setSpecies(speciesRepository.findById(param.getInt("speciesId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 speciesId")));
        lostCase.setBreed(breedRepository.findById(param.getInt("breedId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 breedId")));
        lostCase.setFurColor(furColorRepository.findById(param.getInt("furColorId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 furColorId")));
        lostCase.setCity(cityRepository.findById(param.getInt("cityId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 cityId")));
        lostCase.setDistinctArea(distinctAreaRepository.findById(param.getInt("distinctAreaId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 distinctAreaId")));
        lostCase.setStreet(param.getString("street"));
        lostCase.setGender(param.optString("gender", null));
        lostCase.setSterilization(param.getString("sterilization"));
        lostCase.setAge(param.optInt("age", -1));
        lostCase.setMicroChipNumber(param.optInt("microChipNumber", -1));
        lostCase.setLatitude(param.getBigDecimal("latitude"));
        lostCase.setLongitude(param.getBigDecimal("longitude"));
        lostCase.setDonationAmount(param.optInt("donationAmount", 0));
        lostCase.setCaseState(caseStateRepository.findById(param.getInt("caseStateId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 caseStateId")));
        lostCase.setLostExperience(param.optString("lostExperience", null));
        lostCase.setContactInformation(param.optString("contactInformation", null));
        lostCase.setFeatureDescription(param.optString("featureDescription", null));
        lostCase.setCaseUrl(param.optString("caseUrl", null));
        lostCase.setPublicationTime(LocalDateTime.now());
        lostCase.setLastUpdateTime(LocalDateTime.now());

        // 先存儲 LostCase
        LostCase savedLostCase = lostCaseRepository.save(lostCase);

        // 🔴 檢查這段是否存在：確保 LostCase 建立後自動產生 Banner
        Banner banner = new Banner();
        banner.setLostCase(savedLostCase);
        banner.setBannerType(BannerType.LOST);
        banner.setOnlineDate(LocalDateTime.now());
        banner.setDueDate(LocalDateTime.now().plusDays(30));
        banner.setIsHidden(false);
        bannerRepository.save(banner);

        return savedLostCase;
    }

    /**
     * 根據 ID 查詢 LostCase
     */
    public Optional<LostCase> findById(Integer lostCaseId) {
        return lostCaseRepository.findById(lostCaseId);
    }

    /**
     * 根據 ID 刪除 LostCase，並刪除對應的 Banner
     */
    public void delete(Integer lostCaseId) {
        if (!lostCaseRepository.existsById(lostCaseId)) {
            throw new IllegalArgumentException("LostCase 不存在");
        }

        // 先刪除 Banner
        bannerService.deleteBannerByCaseId(lostCaseId, BannerType.LOST);

        // 再刪除 LostCase
        lostCaseRepository.deleteById(lostCaseId);
    }

    /**
     * 更新 LostCase 的資訊
     */
    public LostCase modify(Integer lostCaseId, JSONObject param) {
        LostCase lostCase = lostCaseRepository.findById(lostCaseId)
                .orElseThrow(() -> new IllegalArgumentException("LostCase 不存在"));

        lostCase.setCaseTitle(param.optString("caseTitle", lostCase.getCaseTitle()));
        lostCase.setGender(param.optString("gender", lostCase.getGender()));
        lostCase.setSterilization(param.optString("sterilization", lostCase.getSterilization()));
        lostCase.setAge(param.has("age") ? param.getInt("age") : lostCase.getAge());
        lostCase.setMicroChipNumber(
                param.has("microChipNumber") ? param.getInt("microChipNumber") : lostCase.getMicroChipNumber());
        lostCase.setLatitude(param.has("latitude") ? param.getBigDecimal("latitude") : lostCase.getLatitude());
        lostCase.setLongitude(param.has("longitude") ? param.getBigDecimal("longitude") : lostCase.getLongitude());
        lostCase.setDonationAmount(
                param.has("donationAmount") ? param.getInt("donationAmount") : lostCase.getDonationAmount());
        lostCase.setLostExperience(param.optString("lostExperience", lostCase.getLostExperience()));
        lostCase.setContactInformation(param.optString("contactInformation", lostCase.getContactInformation()));
        lostCase.setFeatureDescription(param.optString("featureDescription", lostCase.getFeatureDescription()));
        lostCase.setCaseUrl(param.optString("caseUrl", lostCase.getCaseUrl()));
        lostCase.setLastUpdateTime(LocalDateTime.now());

        return lostCaseRepository.save(lostCase);
    }

    public long count(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return lostCaseRepository.count(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<LostCase> find(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return lostCaseRepository.find(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
