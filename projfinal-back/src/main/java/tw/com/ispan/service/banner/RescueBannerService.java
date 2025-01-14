package tw.com.ispan.service.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.banner.RescueBanner;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.banner.RescueBannerRepository;

@Service
@Transactional
public class RescueBannerService {

    @Autowired
    private RescueBannerRepository rescueBannerRepository;
    @Autowired
    private RescueCaseRepository rescueCaseRepository;

    public RescueBanner create(RescueBanner rescueBanner) {
        // 驗證 RescueCase 是否存在
        Optional<RescueCase> rescueCase = rescueCaseRepository.findById(rescueBanner.getRescueCase().getRescueCaseId());
        if (rescueCase.isPresent()) {
            rescueBanner.setRescueCase(rescueCase.get());
            return rescueBannerRepository.save(rescueBanner);
        }
        throw new IllegalArgumentException("RescueCase ID does not exist.");
    }

    public Optional<RescueBanner> update(Integer id, RescueBanner rescueBanner) {
        return rescueBannerRepository.findById(id).map(existingBanner -> {
            Optional<RescueCase> rescueCase = rescueCaseRepository
                    .findById(rescueBanner.getRescueCase().getRescueCaseId());
            if (rescueCase.isPresent()) {
                existingBanner.setOnlineDate(rescueBanner.getOnlineDate());
                existingBanner.setDueDate(rescueBanner.getDueDate());
                existingBanner.setRescueCase(rescueBanner.getRescueCase());
                return rescueBannerRepository.save(existingBanner);
            } else {
                throw new IllegalArgumentException("RescueCase ID does not exist.");
            }
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
