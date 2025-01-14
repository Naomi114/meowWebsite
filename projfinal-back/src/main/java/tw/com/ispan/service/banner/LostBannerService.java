package tw.com.ispan.service.banner;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.banner.LostBanner;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.banner.LostBannerRepository;

@Service
@Transactional
public class LostBannerService {

    @Autowired
    private LostBannerRepository lostBannerRepository;
    @Autowired
    private LostCaseRepository lostCaseRepository;

    public LostBanner create(LostBanner lostBanner) {
        // 驗證 LostCase 是否存在
        Optional<LostCase> lostCase = lostCaseRepository.findById(lostBanner.getLostCase().getLostCaseId());
        if (lostCase.isPresent()) {
            lostBanner.setLostCase(lostCase.get());
            return lostBannerRepository.save(lostBanner);
        }
        throw new IllegalArgumentException("LostCase ID does not exist.");
    }

    public Optional<LostBanner> update(Integer id, LostBanner lostBanner) {
        return lostBannerRepository.findById(id).map(existingBanner -> {
            // 驗證 LostCase 是否存在
            Optional<LostCase> lostCase = lostCaseRepository.findById(lostBanner.getLostCase().getLostCaseId());
            if (lostCase.isPresent()) {
                existingBanner.setLostCase(lostCase.get());
                existingBanner.setOnlineDate(lostBanner.getOnlineDate());
                existingBanner.setDueDate(lostBanner.getDueDate());
                return lostBannerRepository.save(existingBanner);
            } else {
                throw new IllegalArgumentException("LostCase ID does not exist.");
            }
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
