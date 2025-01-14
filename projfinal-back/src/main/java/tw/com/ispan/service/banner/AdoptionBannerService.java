package tw.com.ispan.service.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.banner.AdoptionBanner;
import tw.com.ispan.repository.pet.banner.AdoptionBannerRepository;

@Service
@Transactional
public class AdoptionBannerService {

    @Autowired
    private AdoptionBannerRepository adoptionBannerRepository;
    @Autowired
    private AdoptionCaseRespository adoptionCaseRespository;

    public AdoptionBanner create(AdoptionBanner adoptionBanner) {
        // 驗證 AdoptionCase 是否存在
        Optional<AdoptionCase> adoptionCase = adoptionCaseRespository
                .findById(adoptionBanner.getAdoptionCase().getAdoptionCaseId());
        if (adoptionCase.isPresent()) {
            adoptionCase.setAdoptionCase(adoptionCase.get());
            return adoptionBannerRepository.save(adoptionBanner);
        }
        throw new IllegalArgumentException("AdoptionCase ID does not exist.");
    }

    public Optional<AdoptionBanner> update(Integer id, AdoptionBanner adoptionBanner) {
        return adoptionBannerRepository.findById(id).map(existingBanner -> {
            Optional<AdoptionCase> adoptionCase = adoptionCaseRespository
                    .findById(adoptionBanner.getAdoptionCase().getAdoptionCaseId());
            if (adoptionCase.isPresent()) {
                existingBanner.setOnlineDate(adoptionBanner.getOnlineDate());
                existingBanner.setDueDate(adoptionBanner.getDueDate());
                existingBanner.setAdoptionCase(adoptionBanner.getAdoptionCase());
                return adoptionBannerRepository.save(existingBanner);
            } else {
                throw new IllegalArgumentException("AdoptionCase ID does not exist.");
            }
        });
    }

    public boolean deleteById(Integer id) {
        if (adoptionBannerRepository.existsById(id)) {
            adoptionBannerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}