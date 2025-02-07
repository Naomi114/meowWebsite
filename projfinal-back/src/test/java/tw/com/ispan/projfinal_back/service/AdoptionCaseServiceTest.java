package tw.com.ispan.projfinal_back.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.dto.AdoptioncaseDTO;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistrictAreaRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.forAdopt.AdoptionCaseRepository;
import tw.com.ispan.service.pet.AdoptionCaseService;

@ExtendWith(MockitoExtension.class)
class AdoptionCaseServiceTest {

    @InjectMocks
    private AdoptionCaseService adoptionCaseService;

    @Mock
    private AdoptionCaseRepository adoptionCaseRepository;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private CaseStateRepository caseStateRepository;

    @Mock
    private SpeciesRepository speciesRepository;
    
    @Mock
    private BreedRepository breedRepository;
    
    @Mock
    private CityRepository cityRepository;
    
    @Mock
    private DistrictAreaRepository districtAreaRepository;

    private AdoptioncaseDTO dto;

    @BeforeEach
    void setUp() {
        dto = new AdoptioncaseDTO();
        dto.setCaseTitle("可愛的貓咪需要新家");
        dto.setStory("這是一隻可愛的貓咪，尋找一個愛她的家。");
        dto.setHealthCondition("健康良好");
        dto.setAdoptedCondition("已經進行絕育");
        dto.setNote("0");
        dto.setMemberId(1);
        dto.setCaseStateId(1);
        dto.setSpeciesId(2);
        dto.setBreedId(49);
        dto.setCityId(3);
        dto.setDistrictAreaId(5);
    }

    @Test
    void testCreateAdoptionCase_Success() {
        // 模擬 Repository 回傳查詢結果
        when(memberRepository.findById(1)).thenReturn(Optional.of(new Member()));
        when(caseStateRepository.findById(1)).thenReturn(Optional.of(new CaseState()));
        when(speciesRepository.findById(2)).thenReturn(Optional.of(new Species()));
        when(breedRepository.findById(49)).thenReturn(Optional.of(new Breed()));
        when(cityRepository.findById(3)).thenReturn(Optional.of(new City()));
        when(districtAreaRepository.findById(5)).thenReturn(Optional.of(new DistrictArea()));

        // 模擬 save 行為
        when(adoptionCaseRepository.save(any(AdoptionCase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 測試
        AdoptionCase result = adoptionCaseService.createAdoptionCase(dto);

        // 驗證結果
        assertNotNull(result);
        assertEquals(dto.getCaseTitle(), result.getCaseTitle());
        assertEquals(dto.getStory(), result.getStory());
        assertEquals(dto.getHealthCondition(), result.getHealthCondition());
        assertEquals(dto.getAdoptedCondition(), result.getAdoptedCondition());
        assertEquals(dto.getNote(), result.getNote());
        assertNotNull(result.getLastUpdateTime());
        assertNotNull(result.getPublicationTime());

        // 驗證是否調用 save
        verify(adoptionCaseRepository, times(1)).save(any(AdoptionCase.class));
    }

    @Test
    void testCreateAdoptionCase_MemberNotFound() {
        when(memberRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionCaseService.createAdoptionCase(dto);
        });

        assertEquals("Member ID not found: 1", exception.getMessage());
    }

    @Test
    void testCreateAdoptionCase_CaseStateNotFound() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(new Member()));
        when(caseStateRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionCaseService.createAdoptionCase(dto);
        });

        assertEquals("CaseState ID not found", exception.getMessage());
    }

    @Test
    void testCreateAdoptionCase_SpeciesNotFound() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(new Member()));
        when(caseStateRepository.findById(1)).thenReturn(Optional.of(new CaseState()));
        when(speciesRepository.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionCaseService.createAdoptionCase(dto);
        });

        assertEquals("Species ID not found: 2", exception.getMessage());
    }

    @Test
    void testCreateAdoptionCase_BreedNotFound() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(new Member()));
        when(caseStateRepository.findById(1)).thenReturn(Optional.of(new CaseState()));
        when(speciesRepository.findById(2)).thenReturn(Optional.of(new Species()));
        when(breedRepository.findById(49)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionCaseService.createAdoptionCase(dto);
        });

        assertEquals("Breed ID not found: 49", exception.getMessage());
    }

    @Test
    void testCreateAdoptionCase_CityNotFound() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(new Member()));
        when(caseStateRepository.findById(1)).thenReturn(Optional.of(new CaseState()));
        when(speciesRepository.findById(2)).thenReturn(Optional.of(new Species()));
        when(breedRepository.findById(49)).thenReturn(Optional.of(new Breed()));
        when(cityRepository.findById(3)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionCaseService.createAdoptionCase(dto);
        });

        assertEquals("City ID not found: 3", exception.getMessage());
    }

    @Test
    void testCreateAdoptionCase_DistrictAreaNotFound() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(new Member()));
        when(caseStateRepository.findById(1)).thenReturn(Optional.of(new CaseState()));
        when(speciesRepository.findById(2)).thenReturn(Optional.of(new Species()));
        when(breedRepository.findById(49)).thenReturn(Optional.of(new Breed()));
        when(cityRepository.findById(3)).thenReturn(Optional.of(new City()));
        when(districtAreaRepository.findById(5)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionCaseService.createAdoptionCase(dto);
        });

        assertEquals("DistrictArea ID not found: 5", exception.getMessage());
    }
}
