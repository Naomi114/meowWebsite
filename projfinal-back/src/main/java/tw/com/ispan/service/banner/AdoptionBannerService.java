package tw.com.ispan.service.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.banner.AdoptionBanner;
import tw.com.ispan.repository.pet.banner.AdoptionBannerRepository;

@Service
@Transactional
public class AdoptionBannerService {

    @Autowired
    private AdoptionBannerRepository adoptionBannerRepository;

    public AdoptionBanner create(AdoptionBanner adoptionBanner) {
        return adoptionBannerRepository.save(adoptionBanner);
    }

    public Optional<AdoptionBanner> update(Integer id, AdoptionBanner adoptionBanner) {
        return adoptionBannerRepository.findById(id).map(existingBanner -> {
            existingBanner.setOnlineDate(adoptionBanner.getOnlineDate());
            existingBanner.setDueDate(adoptionBanner.getDueDate());
            existingBanner.setAdoptionCase(adoptionBanner.getAdoptionCase());
            return adoptionBannerRepository.save(existingBanner);
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