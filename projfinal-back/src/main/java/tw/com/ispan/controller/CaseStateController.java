package tw.com.ispan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.repository.pet.CaseStateRepository;

@CrossOrigin(origins = "http://localhost:8080") // 允許來自 Vue 前端的請求
@RestController
public class CaseStateController {

    @Autowired
    private CaseStateRepository caseStatementRepository;

    @GetMapping("/api/casestatement")
    public List<CaseState> getCaseStatements() {
        return caseStatementRepository.findAll();
    }
}
