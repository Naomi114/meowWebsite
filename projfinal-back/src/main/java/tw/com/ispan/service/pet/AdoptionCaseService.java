package tw.com.ispan.service.pet;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistinctArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.dto.AdoptioncaseDTO;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistinctAreaRepository;
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
    private CityRepository cityRepository; // 新的 City repository
    @Autowired
    private DistinctAreaRepository distinctAreaRepository; // 新的 DistinctArea repository
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SpeciesRepository speciesRepository;

    // 新增
    public AdoptionCase createAdoptionCase(AdoptionCase adoptionCase) {

        // 确保传递了 caseTitle
        if (adoptionCase.getCaseTitle() == null || adoptionCase.getCaseTitle().isEmpty()) {
            throw new IllegalArgumentException("CaseTitle must not be null or empty");
        }
        adoptionCase.setCaseTitle(adoptionCase.getCaseTitle()); // 确保值传递到实体

        // 确保传递了 story
        if (adoptionCase.getStory() == null || adoptionCase.getStory().isEmpty()) {
            throw new IllegalArgumentException("Story must not be null or empty");
        }
        adoptionCase.setStory(adoptionCase.getStory()); // 设置 story

        // 确保传递了 healthCondition
        if (adoptionCase.getHealthCondition() == null || adoptionCase.getHealthCondition().isEmpty()) {
            throw new IllegalArgumentException("HealthCondition must not be null or empty");
        }
        adoptionCase.setHealthCondition(adoptionCase.getHealthCondition()); // 设置 healthCondition

        // 确保传递了 adoptedCondition
        if (adoptionCase.getAdoptedCondition() == null || adoptionCase.getAdoptedCondition().isEmpty()) {
            throw new IllegalArgumentException("AdoptedCondition must not be null or empty");
        }
        adoptionCase.setAdoptedCondition(adoptionCase.getAdoptedCondition()); // 设置 adoptedCondition

        // 检查并设置 breedId
        Integer breedId = adoptionCase.getBreed().getBreedId();
        if (breedId == null) {
            throw new IllegalArgumentException("Breed ID must not be null");
        }
        Breed breed = breedRepository.findById(breedId)
                .orElseThrow(() -> new IllegalArgumentException("Breed with ID " + breedId + " not found"));
        adoptionCase.setBreed(breed);

        // 检查并设置 caseStateId
        Integer caseStateId = adoptionCase.getCaseState().getCaseStateId();
        if (caseStateId == null) {
            throw new IllegalArgumentException("CaseState ID must not be null");
        }
        CaseState caseState = caseStateRepository.findById(caseStateId)
                .orElseThrow(() -> new IllegalArgumentException("CaseState with ID " + caseStateId + " not found"));
        adoptionCase.setCaseState(caseState);

        // 查找并设置 City 实体
        Integer cityId = adoptionCase.getCity().getCityId();
        if (cityId == null) {
            throw new IllegalArgumentException("City ID must not be null");
        }
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City with ID " + cityId + " not found"));
        adoptionCase.setCity(city);

        // 查找并设置 DistinctArea 实体
        Integer distinctAreaId = adoptionCase.getDistinctArea().getDistinctAreaId();
        if (distinctAreaId == null) {
            throw new IllegalArgumentException("DistinctArea ID must not be null");
        }
        DistinctArea distinctArea = distinctAreaRepository.findById(distinctAreaId)
                .orElseThrow(
                        () -> new IllegalArgumentException("DistinctArea with ID " + distinctAreaId + " not found"));
        adoptionCase.setDistinctArea(distinctArea);

        // 查找并设置 FurColor 实体
        Integer furColorId = adoptionCase.getFurColor().getFurColorId();
        if (furColorId == null) {
            throw new IllegalArgumentException("FurColor ID must not be null");
        }
        FurColor furColor = furColorRepository.findById(furColorId)
                .orElseThrow(() -> new IllegalArgumentException("FurColor with ID " + furColorId + " not found"));
        adoptionCase.setFurColor(furColor);

        // 查找并设置 Member 实体
        Integer memberId = adoptionCase.getMember().getMemberId();
        if (memberId == null) {
            throw new IllegalArgumentException("Member ID must not be null");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member with ID " + memberId + " not found"));
        adoptionCase.setMember(member);

        // 查找并设置 Species 实体
        Integer speciesId = adoptionCase.getSpecies().getSpeciesId();
        if (speciesId == null) {
            throw new IllegalArgumentException("Species ID must not be null");
        }
        Species species = speciesRepository.findById(speciesId)
                .orElseThrow(() -> new IllegalArgumentException("Species with ID " + speciesId + " not found"));
        adoptionCase.setSpecies(species);

        // 设置最后更新和发布时间
        adoptionCase.setLastUpdateTime(LocalDateTime.now());
        adoptionCase.setPublicationTime(LocalDateTime.now());

        // 保存并返回创建的 AdoptionCase
        return adoptionCaseRepository.save(adoptionCase);
    }

    // -----------------------------------------------------------------------------
    // 貼文者對申請者的註記
    public AdoptionCase updateAdoptionCaseStatusAndNote(Integer adoptionCaseId, AdoptioncaseDTO dto) {
        // 查找 AdoptionCase 是否存在
        AdoptionCase adoptionCase = adoptionCaseRepository.findById(adoptionCaseId)
                .orElseThrow(() -> new RuntimeException("AdoptionCase not found with id " + adoptionCaseId));

        // 更新資料
        adoptionCase.setTitle(dto.getTitle());
        adoptionCase.setNote(dto.getNote());

        // 儲存更新後的資料
        return adoptionCaseRepository.save(adoptionCase);
    }

    // -----------------------------------------------------------------------------
    // 查詢
    public List<AdoptionCase> searchAdoptionCases(Long cityId, Long distinctAreaId, Long caseStateId, Long speciesId,
            String gender) {
        return adoptionCaseRepository.searchAdoptionCases(cityId, distinctAreaId, caseStateId, speciesId, gender);
    }
}
