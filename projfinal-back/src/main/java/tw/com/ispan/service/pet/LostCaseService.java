package tw.com.ispan.service.pet;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.persistence.criteria.Predicate;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.banner.Banner;
import tw.com.ispan.domain.pet.banner.BannerType;
import tw.com.ispan.dto.pet.LostSearchCriteria;
import tw.com.ispan.dto.pet.OutputLostCaseDTO;
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
import tw.com.ispan.util.LatLng;

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
    @Autowired
    private GeocodingService geocodingService;

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
     * @return 遺失案件列表 (DTO)
     */
    public List<OutputLostCaseDTO> getAll(boolean sortDirection) {
        Sort sort = sortDirection ? Sort.by(Sort.Direction.DESC, "lostCaseId")
                : Sort.by(Sort.Direction.ASC, "lostCaseId");

        return lostCaseRepository.findAll(sort).stream()
                .map(OutputLostCaseDTO::new) // ✅ 直接使用 DTO 建構子，讓它處理圖片與會員資訊
                .collect(Collectors.toList());
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

    // 查詢全部
    @Transactional
    public Page<LostCase> searchLostCases(LostSearchCriteria criteria) {
        int start = 0;
        int rows = 10;
        String sortField = "lostCaseId";
        boolean sortDirection = false; // 預設升序

        // ✅ 確保排序欄位合法
        List<String> validSortFields = Arrays.asList("lostCaseId", "speciesId", "breedId", "caseStateId");
        if (criteria.getKeyword() != null && validSortFields.contains(criteria.getKeyword())) {
            sortField = criteria.getKeyword();
        }

        Sort sort = sortDirection ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(start / rows, rows, sort);

        return lostCaseRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // ✅ 文字欄位支援模糊查詢
            if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
                String likePattern = "%" + criteria.getKeyword().trim() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("caseTitle"), likePattern),
                        criteriaBuilder.like(root.get("street"), likePattern),
                        criteriaBuilder.like(root.get("name"), likePattern),
                        criteriaBuilder.like(root.get("gender"), likePattern),
                        criteriaBuilder.like(root.get("featureDescription"), likePattern),
                        criteriaBuilder.like(root.get("contactInformation"), likePattern),
                        criteriaBuilder.like(root.get("lostExperience"), likePattern)));
            }

            // ✅ `gender` 性別篩選
            if (criteria.getGender() != null && !criteria.getGender().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), criteria.getGender()));
            }

            // ✅ `sterilization` 絕育狀態篩選
            if (criteria.getSterilization() != null && !criteria.getSterilization().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sterilization"), criteria.getSterilization()));
            }

            // ✅ `microchip` 10碼晶片篩選
            if ("0123456789".equals(criteria.getMicroChipNumber())) {
                predicates.add(criteriaBuilder.isNotNull(root.get("microChipNumber")));
                predicates.add(criteriaBuilder.notEqual(root.get("microChipNumber"), ""));
            }

            // ✅ 支援單一 speciesId 查詢
            if (criteria.getSpeciesId() != null && criteria.getSpeciesId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("species").get("speciesId"), criteria.getSpeciesId()));
            }

            // ✅ 支援多個 speciesIds 查詢（未來擴展）
            // if (criteria.getSpeciesIds() != null && !criteria.getSpeciesIds().isEmpty())
            // {
            // CriteriaBuilder.In<Integer> inClause =
            // criteriaBuilder.in(root.get("species").get("speciesId"));
            // for (Integer speciesId : criteria.getSpeciesIds()) {
            // inClause.value(speciesId);
            // }
            // predicates.add(inClause);
            // }

            // ✅ 其他篩選條件
            if (criteria.getBreedId() != null && criteria.getBreedId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("breed").get("breedId"), criteria.getBreedId()));
            }
            if (criteria.getFurColorId() != null && criteria.getFurColorId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("furColor").get("furColorId"), criteria.getFurColorId()));
            }
            if (criteria.getCityId() != null && criteria.getCityId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("city").get("cityId"), criteria.getCityId()));
            }
            if (criteria.getDistrictAreaId() != null && criteria.getDistrictAreaId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("districtArea").get("districtAreaId"),
                        criteria.getDistrictAreaId()));
            }
            if (criteria.getCaseStateId() != null && criteria.getCaseStateId() > 0) {
                predicates.add(
                        criteriaBuilder.equal(root.get("caseState").get("caseStateId"), criteria.getCaseStateId()));
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

        // 設置經緯度
        String adress = city.getCity() + districtArea.getDistrictAreaName()
                + param.getString("street");
        System.out.println(adress);
        try {
            LatLng latLng = geocodingService.getCoordinatesFromAddress(adress);
            if (latLng != null) {
                lostCase.setLatitude(latLng.getLat());
                lostCase.setLongitude(latLng.getLng());
            }
        } catch (JsonProcessingException e) {
            System.out.println("請求座標API失敗");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("不支援編碼");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("編碼格式錯誤");
            e.printStackTrace();
        }

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
     * 根據 ID 查詢 LostCase，並轉換為 DTO
     *
     * @param lostCaseId 遺失案件 ID
     * @return Optional<OutputLostCaseDTO> (如果找不到則返回空)
     */
    public Optional<OutputLostCaseDTO> findById(Integer lostCaseId) {
        return lostCaseRepository.findById(lostCaseId)
                .map(OutputLostCaseDTO::new); // ✅ 直接使用 DTO 建構子，讓它處理圖片與會員資訊
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

    // 不分頁查詢所有案件(給googlemap使用)
    public List<LostCase> getAllCases() {
        return lostCaseRepository.findAll();
    }
}
