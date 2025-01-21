package tw.com.ispan.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.service.pet.ReportCaseService;

@RestController
@RequestMapping("/reports")
public class ReportCaseController {

    @Autowired
    private ReportCaseService reportCaseService;

}
