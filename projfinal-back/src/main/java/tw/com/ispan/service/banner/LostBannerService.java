package tw.com.ispan.service.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.banner.LostBanner;
import tw.com.ispan.repository.pet.banner.LostBannerRepository;

@Service
@Transactional
public class LostBannerService {

    @Autowired
    private LostBannerRepository lostBannerRepository;

    public LostBanner create(LostBanner lostBanner) {
        return lostBannerRepository.save(lostBanner);
    }

    public Optional<LostBanner> update(Integer id, LostBanner lostBanner) {
        return lostBannerRepository.findById(id).map(existingBanner -> {
            existingBanner.setOnlineDate(lostBanner.getOnlineDate());
            existingBanner.setDueDate(lostBanner.getDueDate());
            existingBanner.setLostCase(lostBanner.getLostCase());
            return lostBannerRepository.save(existingBanner);
        });
    }

    public boolean deleteById(Integer id) {
        if (lostBannerRepository.existsById(id)) {
            lostBannerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
