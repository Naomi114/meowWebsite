package tw.com.ispan.service.pet;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistinctArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.domain.pet.forRescue.CanAfford;
import tw.com.ispan.domain.pet.forRescue.RescueDemand;
import tw.com.ispan.dto.CityDTO;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistinctAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.forRescue.CanAffordRepository;
import tw.com.ispan.repository.pet.forRescue.RescueDemandRepository;

@Service
@Transactional
public class VForService {

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private FurColorRepository furColorRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistinctAreaRepository districtAreaRepository;
    @Autowired
    private BreedRepository BreedRepository;
    @Autowired
    private CaseStateRepository caseStateRepository;
    @Autowired
    private RescueDemandRepository rescueDemandRepository;
    @Autowired
    private CanAffordRepository canAffordRepository;

    // 尋找全部species
    public List<Species> allSpecies() {
        return speciesRepository.findAll();
    }

    // 尋找全部furColor
    public List<FurColor> allFurColor() {
        return furColorRepository.findAll();
    }

    // 尋找全部city
    public List<CityDTO> allCity() {
        List<City> cities = cityRepository.findAll(); // 獲取所有城市
        // 將City物件轉換為CityDTO
        return cities.stream()
                .map(city -> new CityDTO(city.getCityId(), city.getCity()))
                .collect(Collectors.toList());
    }

    // 依據 cityId 查詢對應的 DistinctArea
    public List<DistinctArea> getDistrictsByCity(Integer cityId) {
        return districtAreaRepository.findByCity_CityId(cityId); // 根據 cityId 查詢區域
    }

    // 尋找全部breed
    public List<Breed> allBreed() {
        return BreedRepository.findAll();
    }

    // 尋找全部救援狀態caseStatus
    public List<CaseState> allCaseState() {
        return caseStateRepository.findAll();
    }

    // 查詢所有rescueDemands
    public List<RescueDemand> allRescueDemands() {
        return rescueDemandRepository.findAll();
    }

    // 查詢所有可負擔事項canAffords
    public List<CanAfford> allCanAffords() {
        return canAffordRepository.findAll();
    }
}