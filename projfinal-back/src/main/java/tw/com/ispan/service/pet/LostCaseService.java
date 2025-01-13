package tw.com.ispan.service.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.repository.pet.LostCaseRepository;

@Service
@Transactional
public class LostCaseService {
    @Autowired
    private LostCaseRepository lostCaseRepository;
}
