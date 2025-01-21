package tw.com.ispan.service.pet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistinctArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.domain.pet.banner.LostBanner;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistinctAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.banner.LostBannerRepository;
import tw.com.ispan.service.banner.LostBannerService;

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
    private LostBannerRepository lostBannerRepository;

    @Autowired
    private LostBannerService lostBannerService;

    public List<LostCase> select(LostCase condition) {
        if (condition == null) {
            return lostCaseRepository.findAll(); // 无条件查询
        }

        // 动态构建查询条件
        Specification<LostCase> spec = Specification.where(null);

        if (condition.getLostCaseId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("lostCaseId"), condition.getLostCaseId()));
        }

        if (condition.getMember() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("memberId"), condition.getMember()));
        }

        if (condition.getCaseTitle() != null && !condition.getCaseTitle().trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("caseTitle"), "%" + condition.getCaseTitle() + "%"));
        }

        if (condition.getCity() != null && condition.getCity().getCityId() != null) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("city").get("cityId"), condition.getCity().getCityId()));
        }

        return lostCaseRepository.findAll(spec);
    }

    public LostCase insert(LostCase bean) {
        if (bean != null && bean.getLostCaseId() != null) {
            if (!lostCaseRepository.existsById(bean.getLostCaseId())) {
                return lostCaseRepository.save(bean);
            }
        }
        return null;
    }

    public boolean delete(LostCase bean) {
        if (bean != null && bean.getLostCaseId() != null) {
            if (lostCaseRepository.existsById(bean.getLostCaseId())) {
                lostCaseRepository.deleteById(bean.getLostCaseId());
                return true;
            }
        }
        return false;
    }

    public LostCase findById(Integer id) {
        if (id != null) {
            Optional<LostCase> optional = lostCaseRepository.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
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

    public boolean exists(Integer lostCaseId) {
        if (lostCaseId == null) {
            return false;
        }
        return lostCaseRepository.existsById(lostCaseId);
    }

    public boolean remove(Integer lostCaseId) {
        if (lostCaseId == null || !lostCaseRepository.existsById(lostCaseId)) {
            return false; // 如果 ID 为 null 或不存在，返回 false
        }
        lostCaseRepository.deleteById(lostCaseId); // 执行删除操作
        return true;
    }

    public LostCase create(String json) {
        try {
            JSONObject obj = new JSONObject(json);

            // 解析 JSON 参数
            String caseTitle = obj.optString("caseTitle");
            // Integer memberId = obj.optInt("memberId");
            Integer speciesId = obj.optInt("speciesId");
            Integer breedId = obj.optInt("breedId");
            Integer furColorId = obj.optInt("furColorId");
            Integer cityId = obj.optInt("cityId");
            Integer distinctAreaId = obj.optInt("distinctAreaId");
            String street = obj.optString("street");
            String gender = obj.optString("gender", null); // 可选字段
            String sterilization = obj.optString("sterilization");
            Integer age = obj.optInt("age", -1);
            Integer microChipNumber = obj.optInt("microChipNumber", -1);
            boolean suspLost = obj.optBoolean("suspLost", false);
            BigDecimal latitude = obj.optBigDecimal("latitude", null);
            BigDecimal longitude = obj.optBigDecimal("longitude", null);
            Integer donationAmount = obj.optInt("donationAmount", 0);
            Integer caseStateId = obj.optInt("caseStateId");
            String lostExperience = obj.optString("lostExperience", null);
            String contactInformation = obj.optString("contactInformation", null);
            String featureDescription = obj.optString("featureDescription", null);
            String caseUrl = obj.optString("caseUrl", null);

            // 验证必填字段
            if (caseTitle == null || speciesId == null || breedId == null || furColorId == null
                    || cityId == null
                    || distinctAreaId == null || street == null || sterilization == null || latitude == null
                    || longitude == null || caseStateId == null) {
                throw new IllegalArgumentException("必填字段不能为空！");
            }

            // 查询关联对象
            // Member member = memberRepository.findById(memberId)
            // .orElseThrow(() -> new IllegalArgumentException("无效的 memberId"));
            Species species = speciesRepository.findById(speciesId)
                    .orElseThrow(() -> new IllegalArgumentException("无效的 speciesId"));
            Breed breed = breedRepository.findById(breedId)
                    .orElseThrow(() -> new IllegalArgumentException("无效的 breedId"));
            FurColor furColor = furColorRepository.findById(furColorId)
                    .orElseThrow(() -> new IllegalArgumentException("无效的 furColorId"));
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new IllegalArgumentException("无效的 cityId"));
            DistinctArea distinctArea = distinctAreaRepository.findById(distinctAreaId)
                    .orElseThrow(() -> new IllegalArgumentException("无效的 distinctAreaId"));
            CaseState caseState = caseStateRepository.findById(caseStateId)
                    .orElseThrow(() -> new IllegalArgumentException("无效的 caseStateId"));

            // 构建实体
            LostCase lostCase = new LostCase();
            lostCase.setCaseTitle(caseTitle);
            // lostCase.setMember(member);
            lostCase.setSpecies(species);
            lostCase.setBreed(breed);
            lostCase.setFurColor(furColor);
            lostCase.setCity(city);
            lostCase.setDistinctArea(distinctArea);
            lostCase.setStreet(street);
            lostCase.setGender(gender);
            lostCase.setSterilization(sterilization);
            lostCase.setAge(age == -1 ? null : age);
            lostCase.setMicroChipNumber(microChipNumber == -1 ? null : microChipNumber);
            lostCase.setSuspLost(suspLost);
            lostCase.setLatitude(latitude);
            lostCase.setLongitude(longitude);
            lostCase.setDonationAmount(donationAmount);
            lostCase.setCaseState(caseState);
            lostCase.setLostExperience(lostExperience);
            lostCase.setContactInformation(contactInformation);
            lostCase.setFeatureDescription(featureDescription);
            lostCase.setCaseUrl(caseUrl);
            lostCase.setPublicationTime(LocalDateTime.now());
            lostCase.setLastUpdateTime(LocalDateTime.now());

            // 創建對應的 LostBanner
            LostBanner lostBanner = new LostBanner();
            lostBanner.setLostCase(lostCase);
            lostBanner.setOnlineDate(LocalDateTime.now());
            lostBanner.setDueDate(LocalDateTime.now().plusDays(30)); // 設置廣告到期時間
            lostBannerRepository.save(lostBanner);

            // 保存并返回
            return lostCaseRepository.save(lostCase);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LostCase modify(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            Integer lostCaseId = obj.isNull("lostCaseId") ? null : obj.getInt("lostCaseId");

            if (lostCaseId != null) {
                Optional<LostCase> optional = lostCaseRepository.findById(lostCaseId);
                if (optional.isPresent()) {
                    LostCase update = optional.get();

                    // 更新字段
                    update.setCaseTitle(obj.optString("caseTitle", update.getCaseTitle()));
                    update.setSpecies(speciesRepository.findById(obj.optInt("speciesId")).orElse(update.getSpecies()));
                    update.setBreed(breedRepository.findById(obj.optInt("breedId")).orElse(update.getBreed()));
                    update.setFurColor(
                            furColorRepository.findById(obj.optInt("furColorId")).orElse(update.getFurColor()));
                    update.setCity(cityRepository.findById(obj.optInt("cityId")).orElse(update.getCity()));
                    update.setDistinctArea(distinctAreaRepository.findById(obj.optInt("distinctAreaId"))
                            .orElse(update.getDistinctArea()));
                    update.setStreet(obj.optString("street", update.getStreet()));
                    update.setGender(obj.optString("gender", update.getGender()));
                    update.setSterilization(obj.optString("sterilization", update.getSterilization()));
                    update.setAge(obj.optInt("age", update.getAge()));
                    update.setMicroChipNumber(obj.optInt("microChipNumber", update.getMicroChipNumber()));
                    update.setSuspLost(obj.optBoolean("suspLost", update.isSuspLost()));
                    update.setLatitude(obj.optBigDecimal("latitude", update.getLatitude()));
                    update.setLongitude(obj.optBigDecimal("longitude", update.getLongitude()));
                    update.setDonationAmount(obj.optInt("donationAmount", update.getDonationAmount()));
                    update.setCaseState(
                            caseStateRepository.findById(obj.optInt("caseStateId")).orElse(update.getCaseState()));
                    update.setLostExperience(obj.optString("lostExperience", update.getLostExperience()));
                    update.setContactInformation(obj.optString("contactInformation", update.getContactInformation()));
                    update.setFeatureDescription(obj.optString("featureDescription", update.getFeatureDescription()));
                    update.setCaseUrl(obj.optString("caseUrl", update.getCaseUrl()));
                    update.setLastUpdateTime(LocalDateTime.now());

                    return lostCaseRepository.save(update);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 創建 LostCase 並自動創建對應的 LostBanner
     */
    public LostCase createLostCaseWithBanner(LostCase lostCase, LocalDateTime onlineDate, LocalDateTime dueDate) {
        LostCase savedLostCase = lostCaseRepository.save(lostCase);

        // 創建對應的廣告牆
        lostBannerService.createBannerForLostCase(savedLostCase.getLostCaseId(), onlineDate, dueDate);

        return savedLostCase;
    }
}
