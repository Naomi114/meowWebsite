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
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.dto.AdoptioncaseDTO;
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
    private CityRepository cityRepository; // 新的 City repository
    @Autowired
    private DistrictAreaRepository districtAreaRepository; // 新的 districtArea repository
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SpeciesRepository speciesRepository;

    // 新增
    public AdoptionCase createAdoptionCase(AdoptioncaseDTO dto) {
        AdoptionCase adoptionCase = new AdoptionCase();
        
        adoptionCase.setCaseTitle(dto.getCaseTitle());
        adoptionCase.setStory(dto.getStory());
        adoptionCase.setHealthCondition(dto.getHealthCondition());
        adoptionCase.setAdoptedCondition(dto.getAdoptedCondition());
        adoptionCase.setNote(dto.getNote());
    
        // 查找關聯物件並設置
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member ID not found: " + dto.getMemberId()));
        adoptionCase.setMember(member);
    
        CaseState caseState = caseStateRepository.findById(dto.getCaseStateId())
        .orElseThrow(() -> new IllegalArgumentException("CaseState ID not found"));
        adoptionCase.setCaseState(caseState);

        Species species = speciesRepository.findById(dto.getSpeciesId())
                .orElseThrow(() -> new IllegalArgumentException("Species ID not found: " + dto.getSpeciesId()));
        adoptionCase.setSpecies(species);
    
        Breed breed = breedRepository.findById(dto.getBreedId())
                .orElseThrow(() -> new IllegalArgumentException("Breed ID not found: " + dto.getBreedId()));
        adoptionCase.setBreed(breed);
    
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City ID not found: " + dto.getCityId()));
        adoptionCase.setCity(city);
    
        DistrictArea districtArea = districtAreaRepository.findById(dto.getDistrictAreaId())
                .orElseThrow(() -> new IllegalArgumentException("DistrictArea ID not found: " + dto.getDistrictAreaId()));
        adoptionCase.setDistrictArea(districtArea);
    
        adoptionCase.setLastUpdateTime(LocalDateTime.now());
        adoptionCase.setPublicationTime(LocalDateTime.now());
    
        return adoptionCaseRepository.save(adoptionCase);
    }    

    // -----------------------------------------------------------------------------
    // 貼文者對申請者的註記
    public AdoptionCase updateAdoptionCaseStatusAndNote(Integer adoptionCaseId, AdoptioncaseDTO dto) {
        // 查找 AdoptionCase 是否存在
        AdoptionCase adoptionCase = adoptionCaseRepository.findById(adoptionCaseId)
                .orElseThrow(() -> new RuntimeException("AdoptionCase not found with id " + adoptionCaseId));

        // 更新資料
        adoptionCase.setCaseTitle(dto.getCaseTitle());
        adoptionCase.setNote(dto.getNote());

        // 儲存更新後的資料
        return adoptionCaseRepository.save(adoptionCase);
    }

    // -----------------------------------------------------------------------------
    // 查詢
    public List<AdoptionCase> searchAdoptionCases(Long cityId, Long districtAreaId, Long caseStateId, Long speciesId,
            String gender) {
        return adoptionCaseRepository.searchAdoptionCases(cityId, districtAreaId, caseStateId, speciesId, gender);
    }
}
