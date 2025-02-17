package tw.com.ispan.service.pet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.dto.pet.AdoptioncaseDTO;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistrictAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseRepository;

@Service
public class AdoptionCaseService {
        @Autowired
        private AdoptionCaseRepository adoptionCaseRepository;
        @Autowired
        private CaseStateRepository caseStateRepository;
        @Autowired
        private BreedRepository breedRepository;
        @Autowired
        private FurColorRepository furColorRepository;
        @Autowired
        private CityRepository cityRepository;
        @Autowired
        private DistrictAreaRepository districtAreaRepository;
        @Autowired
        private MemberRepository memberRepository;
        @Autowired
        private SpeciesRepository speciesRepository;

        // 新增
        public AdoptionCase createAdoptionCase(JSONObject param) {
                System.out.println("接收到的 AdoptionCase: " + param);
                AdoptionCase adoptionCase = new AdoptionCase();

                // 其他設置屬性
                adoptionCase.setCaseTitle(param.getString("caseTitle"));

                // 設置 member、species、breed 等
                adoptionCase.setMember(memberRepository.findById(param.getInt("memberId"))
                                .orElseThrow(() -> new IllegalArgumentException("無效的 memberId")));
                adoptionCase.setSpecies(speciesRepository.findById(param.getInt("speciesId"))
                                .orElseThrow(() -> new IllegalArgumentException("無效的 speciesId")));
                adoptionCase.setBreed(breedRepository.findById(param.getInt("breedId"))
                                .orElseThrow(() -> new IllegalArgumentException("無效的 breedId")));
                adoptionCase.setFurColor(furColorRepository.findById(param.getInt("furColorId"))
                                .orElseThrow(() -> new IllegalArgumentException("無效的 furColorId")));

                // 設置 City 和 DistrictArea
                City city = cityRepository.findById(param.getInt("cityId"))
                                .orElseThrow(() -> new IllegalArgumentException("無效的 cityId"));
                adoptionCase.setCity(city);

                DistrictArea districtArea = districtAreaRepository.findById(param.getInt("districtAreaId"))
                                .orElseThrow(() -> new IllegalArgumentException("無效的 districtAreaId"));
                adoptionCase.setDistrictArea(districtArea);

                // adoptionCase.setStreet(param.getString("street"));
                adoptionCase.setGender(param.optString("gender", null));
                adoptionCase.setSterilization(param.getString("sterilization"));
                adoptionCase.setAge(param.optInt("age", -1));
                adoptionCase.setMicroChipNumber(param.optInt("microChipNumber", -1));
                // adoptionCase.setLatitude(param.getDouble("latitude"));
                // adoptionCase.setLongitude(param.getDouble("longitude"));

                adoptionCase.setCaseState(caseStateRepository.findById(5)
                                .orElseThrow(() -> new IllegalArgumentException("案件狀態不存在")));

                // 設置 healthCondition 和 adoptedCondition
                adoptionCase.setHealthCondition(param.optString("healthCondition", null));
                adoptionCase.setAdoptedCondition(param.optString("adoptedCondition", null));

                // 設置 story
                adoptionCase.setStory(param.optString("story", "No story provided"));

                // **設置 name**
                adoptionCase.setName(param.optString("name", null)); // 加入此行來設置 name

                adoptionCase.setPublicationTime(LocalDateTime.now());
                adoptionCase.setLastUpdateTime(LocalDateTime.now());

                AdoptionCase savedAdoptionCase = adoptionCaseRepository.save(adoptionCase);

                return savedAdoptionCase;
        }

        // -----------------------------------------------------------------------------
        // 貼文者對申請者的註記
        public AdoptionCase updateAdoptionCaseStatusAndNote(Integer adoptionCaseId, AdoptioncaseDTO dto) {
                // 查找 AdoptionCase 是否存在
                AdoptionCase adoptionCase = adoptionCaseRepository.findById(adoptionCaseId)
                                .orElseThrow(() -> new RuntimeException(
                                                "AdoptionCase not found with id " + adoptionCaseId));

                // 更新資料
                adoptionCase.setApplyTitle(dto.getApplyTitle());
                adoptionCase.setNote(dto.getNote());

                // 儲存更新後的資料
                return adoptionCaseRepository.save(adoptionCase);
        }

        // -----------------------------------------------------------------------------
        // 查詢
        @Transactional(readOnly = true)
        public Page<AdoptionCase> searchAdoptionCases(JSONObject param) {
                int start = param.optInt("start", 0); // 預設從第 0 筆開始
                int rows = param.optInt("rows", 10); // 預設每頁 10 筆
                String sortField = param.optString("sort", "adoptionCaseId"); // 預設排序欄位
                boolean sortDirection = param.optBoolean("dir", false); // false = 升序，true = 降序

                // 設定分頁與排序
                Sort sort = sortDirection ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
                Pageable pageable = PageRequest.of(start / rows, rows, sort);

                // 使用 Specification 進行條件查詢
                return adoptionCaseRepository.findAll((root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        // 模糊查詢 caseTitle
                        if (param.has("caseTitle") && !param.getString("caseTitle").isEmpty()) {
                                String likePattern = "%" + param.getString("caseTitle") + "%";
                                predicates.add(criteriaBuilder.like(root.get("caseTitle"), likePattern));
                        }

                        // 根據 speciesId 查詢
                        if (param.has("speciesId")) {
                                predicates.add(criteriaBuilder.equal(root.get("species").get("speciesId"),
                                                param.getInt("speciesId")));
                        }

                        // 根據 breedId 查詢
                        if (param.has("breedId")) {
                                predicates.add(criteriaBuilder.equal(root.get("breed").get("breedId"),
                                                param.getInt("breedId")));
                        }

                        // 根據 furColorId 查詢
                        if (param.has("furColorId")) {
                                predicates
                                                .add(criteriaBuilder.equal(root.get("furColor").get("furColorId"),
                                                                param.getInt("furColorId")));
                        }

                        // 根據 cityId 查詢
                        if (param.has("cityId")) {
                                predicates.add(criteriaBuilder.equal(root.get("city").get("cityId"),
                                                param.getInt("cityId")));
                        }

                        // 根據 districtAreaId 查詢
                        if (param.has("districtAreaId")) {
                                predicates.add(criteriaBuilder.equal(root.get("districtArea").get("districtAreaId"),
                                                param.getInt("districtAreaId")));
                        }

                        // 根據案件狀態 caseStateId 查詢
                        if (param.has("caseStateId")) {
                                predicates.add(
                                                criteriaBuilder.equal(root.get("caseState").get("caseStateId"),
                                                                param.getInt("caseStateId")));
                        }

                        // 查詢未隱藏的案件
                        predicates.add(criteriaBuilder.equal(root.get("isHidden"), false));

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);
        }

        // 修改
        public AdoptionCase modify(Integer adoptionCaseId, JSONObject param) {

                AdoptionCase adoptionCase = adoptionCaseRepository.findById(adoptionCaseId)
                                .orElseThrow(() -> new IllegalArgumentException("LostCase 不存在"));

                // 修改 caseTitle
                adoptionCase.setCaseTitle(param.optString("caseTitle", adoptionCase.getCaseTitle()));

                // 修改 applyTitle
                adoptionCase.setApplyTitle(param.optString("applyTitle", adoptionCase.getApplyTitle()));

                // 修改 note
                adoptionCase.setNote(param.optString("note", adoptionCase.getNote()));

                // 修改 gender
                adoptionCase.setGender(param.optString("gender", adoptionCase.getGender()));

                // 修改 sterilization
                adoptionCase.setSterilization(param.optString("sterilization", adoptionCase.getSterilization()));

                // 修改 age
                adoptionCase.setAge(param.has("age") ? param.getInt("age") : adoptionCase.getAge());

                // 修改 microChipNumber
                adoptionCase.setMicroChipNumber(param.has("microChipNumber") ? param.getInt("microChipNumber")
                                : adoptionCase.getMicroChipNumber());

                // 修改 latitude
                // adoptionCase.setLatitude(
                // param.has("latitude") ? param.getDouble("latitude") :
                // adoptionCase.getLatitude());

                // 修改 longitude
                // adoptionCase.setLongitude(
                // param.has("longitude") ? param.getDouble("longitude") :
                // adoptionCase.getLongitude());

                // 修改 donationAmount
                adoptionCase.setDonationAmount(param.has("donationAmount") ? param.getInt("donationAmount")
                                : adoptionCase.getDonationAmount());

                // 修改 caseUrl
                adoptionCase.setCaseUrl(param.optString("caseUrl", adoptionCase.getCaseUrl()));

                // 更新 lastUpdateTime
                adoptionCase.setLastUpdateTime(LocalDateTime.now());

                // 保存修改後的 AdoptionCase
                return adoptionCaseRepository.save(adoptionCase);
        }

        // 刪除
        public void delete(Integer adoptionCaseId) {
                if (!adoptionCaseRepository.existsById(adoptionCaseId)) {
                        throw new IllegalArgumentException("AdoptionCase 不存在");
                }

                // 再刪除 LostCase
                adoptionCaseRepository.deleteById(adoptionCaseId);
        }

        // 查詢一筆
        public Optional<AdoptionCase> findById(Integer adoptionCaseId) {
                return adoptionCaseRepository.findById(adoptionCaseId);
        }
}
