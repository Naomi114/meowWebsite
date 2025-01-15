package tw.com.ispan.service.pet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistinctAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;

@Service
@Transactional
public class LostCaseService {
    @Autowired
    private LostCaseRepository lostCaseRepository; // 假設有 JPA Repository

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

    public List<LostCase> select(LostCase bean) {
        List<LostCase> result = null;
        if (bean != null && bean.getLostCaseId() != null && !bean.getLostCaseId().equals(0)) {
            Optional<LostCase> optional = lostCaseRepository.findById(bean.getLostCaseId());
            if (optional.isPresent()) {
                result = new ArrayList<LostCase>();
                result.add(optional.get());
            }
        } else {
            result = lostCaseRepository.findAll();
        }
        return result;
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

    public boolean exists(Integer id) {
        if (id != null) {
            return lostCaseRepository.existsById(id);
        }
        return false;
    }

    public boolean remove(Integer id) {
        if (id != null && lostCaseRepository.existsById(id)) { // 驗證 ID 是否存在
            lostCaseRepository.deleteById(id); // 使用 Repository 刪除對應的資料
            return true; // 成功刪除，回傳 true
        }
        return false; // 若 ID 為空或資料不存在，回傳 false
    }

    public long count(String json) {
        try {
            JSONObject obj = new JSONObject(json); // 將 JSON 字串轉換為 JSONObject
            return lostCaseRepository.count(obj); // 調用 Repository 的 count 方法進行統計
        } catch (Exception e) {
            e.printStackTrace(); // 捕捉並記錄例外情況
        }
        return 0; // 若發生例外，返回 0
    }

    public List<LostCase> find(String json) {
        try {
            JSONObject obj = new JSONObject(json); // 將 JSON 字串轉換為 JSONObject
            return lostCaseRepository.find(obj); // 調用 Repository 的 find 方法進行查詢
        } catch (Exception e) {
            e.printStackTrace(); // 捕捉並記錄例外情況
        }
        return null; // 若發生例外，返回 null
    }

    public LostCase create(String json) {
        try {
            // 解析 JSON 字串
            JSONObject obj = new JSONObject(json);

            // 基本欄位解析
            String caseTitle = obj.optString("caseTitle");
            Integer memberId = obj.optInt("memberId");
            Integer speciesId = obj.optInt("speciesId");
            Integer breedId = obj.optInt("breedId");
            Integer furColorId = obj.optInt("furColorId");
            String name = obj.optString("name");
            String gender = obj.optString("gender", null); // 可選欄位
            String sterilization = obj.optString("sterilization");
            Integer age = obj.optInt("age", -1); // -1 表示空值
            Integer microChipNumber = obj.optInt("microChipNumber", -1);
            boolean suspLost = obj.optBoolean("suspLost", false);
            Integer cityId = obj.optInt("cityId");
            Integer distinctAreaId = obj.optInt("distinctAreaId");
            String street = obj.optString("street");
            BigDecimal latitude = obj.optBigDecimal("latitude", null);
            BigDecimal longitude = obj.optBigDecimal("longitude", null);
            Integer donationAmount = obj.optInt("donationAmount", 0);
            Integer viewCount = obj.optInt("viewCount", 0);
            Integer follow = obj.optInt("follow", 0);
            Integer caseStateId = obj.optInt("caseStateId");
            String lostExperience = obj.optString("lostExperience", null);
            String contactInformation = obj.optString("contactInformation", null);
            String featureDescription = obj.optString("featureDescription", null);
            String caseUrl = obj.optString("caseUrl", null);

            // 驗證必填欄位
            if (caseTitle == null || memberId == null || speciesId == null || breedId == null || furColorId == null ||
                    sterilization == null || cityId == null || distinctAreaId == null || street == null
                    || latitude == null || longitude == null || caseStateId == null) {
                throw new IllegalArgumentException("必填欄位不可為空");
            }

            // 查詢關聯物件
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 memberId"));
            Species species = speciesRepository.findById(speciesId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 speciesId"));
            Breed breed = breedRepository.findById(breedId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 breedId"));
            FurColor furColor = furColorRepository.findById(furColorId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 furColorId"));
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 cityId"));
            DistinctArea distinctArea = distinctAreaRepository.findById(distinctAreaId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 distinctAreaId"));
            CaseState caseState = caseStateRepository.findById(caseStateId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的 caseStateId"));

            // 建立 LostCase 實體
            LostCase lostCase = new LostCase();
            lostCase.setCaseTitle(caseTitle);
            lostCase.setMember(member);
            lostCase.setSpecies(species);
            lostCase.setBreed(breed);
            lostCase.setFurColor(furColor);
            lostCase.setName(name);
            lostCase.setGender(gender);
            lostCase.setSterilization(sterilization);
            lostCase.setAge(age == -1 ? null : age); // 設定空值
            lostCase.setMicroChipNumber(microChipNumber == -1 ? null : microChipNumber);
            lostCase.setSuspLost(suspLost);
            lostCase.setCity(city);
            lostCase.setDistinctArea(distinctArea);
            lostCase.setStreet(street);
            lostCase.setLatitude(latitude);
            lostCase.setLongitude(longitude);
            lostCase.setDonationAmount(donationAmount);
            lostCase.setViewCount(viewCount);
            lostCase.setFollow(follow);
            lostCase.setPublicationTime(LocalDateTime.now());
            lostCase.setLastUpdateTime(LocalDateTime.now());
            lostCase.setCaseState(caseState);
            lostCase.setLostExperience(lostExperience);
            lostCase.setContactInformation(contactInformation);
            lostCase.setFeatureDescription(featureDescription);
            lostCase.setCaseUrl(caseUrl);

            // 儲存到資料庫
            return lostCaseRepository.save(lostCase);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LostCase modify(String json) {
        try {
            // 解析 JSON 字串
            JSONObject obj = new JSONObject(json);

            // 提取欄位資料
            Integer lostCaseId = obj.isNull("lostCaseId") ? null : obj.getInt("lostCaseId");
            String caseTitle = obj.optString("caseTitle", null);
            Integer speciesId = obj.isNull("speciesId") ? null : obj.getInt("speciesId");
            Integer breedId = obj.isNull("breedId") ? null : obj.getInt("breedId");
            Integer furColorId = obj.isNull("furColorId") ? null : obj.getInt("furColorId");
            String name = obj.optString("name", null);
            String gender = obj.optString("gender", null);
            String sterilization = obj.optString("sterilization", null);
            Integer age = obj.isNull("age") ? null : obj.getInt("age");
            Integer microChipNumber = obj.isNull("microChipNumber") ? null : obj.getInt("microChipNumber");
            Boolean suspLost = obj.isNull("suspLost") ? null : obj.getBoolean("suspLost");
            Integer cityId = obj.isNull("cityId") ? null : obj.getInt("cityId");
            Integer distinctAreaId = obj.isNull("distinctAreaId") ? null : obj.getInt("distinctAreaId");
            String street = obj.optString("street", null);
            BigDecimal latitude = obj.isNull("latitude") ? null : obj.getBigDecimal("latitude");
            BigDecimal longitude = obj.isNull("longitude") ? null : obj.getBigDecimal("longitude");
            Integer caseStateId = obj.isNull("caseStateId") ? null : obj.getInt("caseStateId");
            String lostExperience = obj.optString("lostExperience", null);
            String contactInformation = obj.optString("contactInformation", null);
            String featureDescription = obj.optString("featureDescription", null);

            // 驗證是否提供了 LostCase ID
            if (lostCaseId != null) {
                Optional<LostCase> optional = lostCaseRepository.findById(lostCaseId);
                if (optional.isPresent()) {
                    LostCase update = optional.get();

                    // 更新欄位資料
                    if (caseTitle != null)
                        update.setCaseTitle(caseTitle);
                    if (speciesId != null) {
                        Species species = speciesRepository.findById(speciesId)
                                .orElseThrow(() -> new IllegalArgumentException("無效的 speciesId"));
                        update.setSpecies(species);
                    }
                    if (breedId != null) {
                        Breed breed = breedRepository.findById(breedId)
                                .orElseThrow(() -> new IllegalArgumentException("無效的 breedId"));
                        update.setBreed(breed);
                    }
                    if (furColorId != null) {
                        FurColor furColor = furColorRepository.findById(furColorId)
                                .orElseThrow(() -> new IllegalArgumentException("無效的 furColorId"));
                        update.setFurColor(furColor);
                    }
                    if (name != null)
                        update.setName(name);
                    if (gender != null)
                        update.setGender(gender);
                    if (sterilization != null)
                        update.setSterilization(sterilization);
                    if (age != null)
                        update.setAge(age);
                    if (microChipNumber != null)
                        update.setMicroChipNumber(microChipNumber);
                    if (suspLost != null)
                        update.setSuspLost(suspLost);
                    if (cityId != null) {
                        City city = cityRepository.findById(cityId)
                                .orElseThrow(() -> new IllegalArgumentException("無效的 cityId"));
                        update.setCity(city);
                    }
                    if (distinctAreaId != null) {
                        DistinctArea distinctArea = distinctAreaRepository.findById(distinctAreaId)
                                .orElseThrow(() -> new IllegalArgumentException("無效的 distinctAreaId"));
                        update.setDistinctArea(distinctArea);
                    }
                    if (street != null)
                        update.setStreet(street);
                    if (latitude != null)
                        update.setLatitude(latitude);
                    if (longitude != null)
                        update.setLongitude(longitude);
                    if (caseStateId != null) {
                        CaseState caseState = caseStateRepository.findById(caseStateId)
                                .orElseThrow(() -> new IllegalArgumentException("無效的 caseStateId"));
                        update.setCaseState(caseState);
                    }
                    if (lostExperience != null)
                        update.setLostExperience(lostExperience);
                    if (contactInformation != null)
                        update.setContactInformation(contactInformation);
                    if (featureDescription != null)
                        update.setFeatureDescription(featureDescription);

                    // 更新最後修改時間
                    update.setLastUpdateTime(LocalDateTime.now());

                    // 儲存更新後的資料
                    return lostCaseRepository.save(update);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
