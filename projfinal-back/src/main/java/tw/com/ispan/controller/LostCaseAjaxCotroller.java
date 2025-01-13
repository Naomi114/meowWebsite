package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.service.pet.LostCaseService;

@RestController
@RequestMapping("/ajax/pages/lostcase")
public class LostCaseAjaxCotroller {
    @Autowired
    private LostCaseService lostCaseService;

    @PostMapping
    public LostCase create(@RequestBody String json) {
        LostCaseResponse responseBean = new LostCaseResponse();
    }
}
