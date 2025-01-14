package tw.com.ispan.service.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.banner.RescueBanner;
import tw.com.ispan.repository.pet.banner.RescueBannerRepository;

@Service
@Transactional
public class RescueBannerService {

    @Autowired
    private RescueBannerRepository rescueBannerRepository;

    public RescueBanner create(RescueBanner rescueBanner) {
        return rescueBannerRepository.save(rescueBanner);
    }

    public Optional<RescueBanner> update(Integer id, RescueBanner rescueBanner) {
        return rescueBannerRepository.findById(id).map(existingBanner -> {
            existingBanner.setOnlineDate(rescueBanner.getOnlineDate());
            existingBanner.setDueDate(rescueBanner.getDueDate());
            existingBanner.setRescueCase(rescueBanner.getRescueCase());
            return rescueBannerRepository.save(existingBanner);
        });
    }

    public boolean deleteById(Integer id) {
        if (rescueBannerRepository.existsById(id)) {
            rescueBannerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
