package tw.com.ispan.service.pet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.banner.Banner;
import tw.com.ispan.domain.pet.banner.BannerType;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BannerRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistrictAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
//import tw.com.ispan.service.banner.BannerService;

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
    private DistrictAreaRepository districtAreaRepository;

    @Autowired
    private CaseStateRepository caseStateRepository;

    // @Autowired
    // private BannerService bannerService;

    @Autowired
    private BannerRepository bannerRepository;

    public void saveLostCase(LostCase lostCase) {
        lostCaseRepository.save(lostCase);
    }

    /**
     * 取得所有遺失案件，並支援排序（新到舊、舊到新）
     *
     * @param sortDirection 排序方向 (true = desc, false = asc)
     * @return 遺失案件列表
     */
    public List<LostCase> getAll(boolean sortDirection) {
        Sort sort = sortDirection ? Sort.by(Sort.Direction.DESC, "lostCaseId")
                : Sort.by(Sort.Direction.ASC, "lostCaseId");
        return lostCaseRepository.findAll(sort);
    }

    /**
     * 根據會員 ID 查詢對應的 LostCases
     *
     * @param memberId 會員 ID
     * @return 該會員的 LostCase 列表
     */
    public List<LostCase> findByMemberId(Integer memberId) {
        return lostCaseRepository.findByMemberId(memberId);
    }

    @Transactional()
    public Page<LostCase> searchLostCases(JSONObject param) {
        int start = param.optInt("start", 0);
        int rows = param.optInt("rows", 10);
        String sortField = param.optString("sort", "lostCaseId");
        boolean sortDirection = param.optBoolean("dir", false);
        lostCaseRepository.flush();

        // ✅ 限制排序欄位，防止錯誤排序
        List<String> validSortFields = Arrays.asList("lostCaseId", "speciesId", "breedId", "caseStateId");
        if (!validSortFields.contains(sortField)) {
            sortField = "lostCaseId";
        }

        Sort sort = sortDirection ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(start / rows, rows, sort);

        return lostCaseRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ✅ 支援模糊查詢 caseTitle + description
            if (param.has("keyword")) {
                String likePattern = "%" + param.getString("keyword") + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("caseTitle"), likePattern),
                        criteriaBuilder.like(root.get("description"), likePattern)));
                System.out.println("🔎 查詢條件：" + param.toString());
            }

            // ✅ 支援單一 speciesId 查詢
            if (param.has("speciesId")) {
                int speciesId = param.optInt("speciesId", -1);
                if (speciesId > 0) {
                    predicates.add(criteriaBuilder.equal(root.get("species").get("speciesId"), speciesId));
                }
            }

            // ✅ 支援多個 speciesIds 查詢
            if (param.has("speciesIds")) {
                JSONArray speciesIds = param.getJSONArray("speciesIds");
                CriteriaBuilder.In<Integer> inClause = criteriaBuilder.in(root.get("species").get("speciesId"));
                for (int i = 0; i < speciesIds.length(); i++) {
                    inClause.value(speciesIds.getInt(i));
                }
                predicates.add(inClause);
            }

            // ✅ 其他篩選條件
            if (param.has("breedId")) {
                predicates.add(criteriaBuilder.equal(root.get("breed").get("breedId"), param.getInt("breedId")));
                System.out.println("🔎 查詢條件：" + param.toString());
            }
            if (param.has("furColorId")) {
                predicates
                        .add(criteriaBuilder.equal(root.get("furColor").get("furColorId"), param.getInt("furColorId")));
                System.out.println("🔎 查詢條件：" + param.toString());
            }
            if (param.has("cityId")) {
                predicates.add(criteriaBuilder.equal(root.get("city").get("cityId"), param.getInt("cityId")));
                System.out.println("🔎 查詢條件：" + param.toString());
            }
            if (param.has("districtAreaId")) {
                predicates.add(criteriaBuilder.equal(root.get("districtArea").get("districtAreaId"),
                        param.getInt("districtAreaId")));
                System.out.println("🔎 查詢條件：" + param.toString());
            }
            if (param.has("caseStateId")) {
                predicates.add(
                        criteriaBuilder.equal(root.get("caseState").get("caseStateId"), param.getInt("caseStateId")));
                System.out.println("🔎 查詢條件：" + param.toString());
            }

            // ✅ 只查詢未隱藏案件
            predicates.add(criteriaBuilder.equal(root.get("isHidden"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * 創建 LostCase 並自動創建對應的 Banner
     */
    public LostCase create(JSONObject param, List<CasePicture> casePictures) {
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

        // 設置 City 和 DistrictArea
        City city = cityRepository.findById(param.getInt("cityId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 cityId"));
        lostCase.setCity(city);

        DistrictArea districtArea = districtAreaRepository.findById(param.getInt("districtAreaId"))
                .orElseThrow(() -> new IllegalArgumentException("無效的 districtAreaId"));
        lostCase.setDistrictArea(districtArea);

        lostCase.setStreet(param.getString("street"));
        lostCase.setName(param.getString("petName"));
        lostCase.setGender(param.optString("gender", null));
        lostCase.setSterilization(param.getString("sterilization"));
        lostCase.setAge(param.optInt("age"));
        lostCase.setMicroChipNumber(param.optString("microChipNumber"));
        // lostCase.setLatitude(param.getDouble("latitude"));
        // lostCase.setLongitude(param.getDouble("longitude"));
        lostCase.setDonationAmount(param.optInt("donationAmount", 0));

        // **固定案件狀態為「待協尋」（caseStateId = 5）**
        lostCase.setCaseState(caseStateRepository.findById(5)
                .orElseThrow(() -> new IllegalArgumentException("案件狀態不存在")));

        lostCase.setLostExperience(param.optString("lostExperience", null));
        lostCase.setContactInformation(param.optString("contactInformation", null));
        lostCase.setFeatureDescription(param.optString("featureDescription", null));
        lostCase.setCaseUrl(param.optString("caseUrl", null));
        lostCase.setPublicationTime(LocalDateTime.now());
        lostCase.setLastUpdateTime(LocalDateTime.now());

        // **關聯圖片**
        lostCase.setCasePictures(casePictures);

        // **先存儲 LostCase**
        LostCase savedLostCase = lostCaseRepository.save(lostCase);

        // **確保 LostCase 建立後自動產生 Banner**
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
        // bannerService.deleteBannerByCaseId(lostCaseId, BannerType.LOST);

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
                param.has("microChipNumber") ? param.optString("microChipNumber") : lostCase.getMicroChipNumber());
        // lostCase.setLatitude(param.has("latitude") ? param.getDouble("latitude") :
        // lostCase.getLatitude());
        // lostCase.setLongitude(param.has("longitude") ? param.getDouble("longitude") :
        // lostCase.getLongitude());
        lostCase.setDonationAmount(
                param.has("donationAmount") ? param.getInt("donationAmount") : lostCase.getDonationAmount());
        lostCase.setLostExperience(param.optString("lostExperience", lostCase.getLostExperience()));
        lostCase.setContactInformation(param.optString("contactInformation", lostCase.getContactInformation()));
        lostCase.setFeatureDescription(param.optString("featureDescription", lostCase.getFeatureDescription()));
        lostCase.setCaseUrl(param.optString("caseUrl", lostCase.getCaseUrl()));
        // 🔹 更新案件狀態（如果有提供）
        if (param.has("caseStateId")) {
            Integer caseStateId = param.getInt("caseStateId");

            // 從資料庫查找對應的 CaseState
            CaseState caseState = caseStateRepository.findById(caseStateId)
                    .orElseThrow(() -> new IllegalArgumentException("CaseState 不存在"));

            lostCase.setCaseState(caseState);
        }

        // 🔹 更新是否隱藏（如果有提供）
        if (param.has("isHidden")) {
            lostCase.setIsHidden(param.getBoolean("isHidden"));
        }
        lostCase.setLastUpdateTime(LocalDateTime.now());

        return lostCaseRepository.save(lostCase);
    }
}
