package tw.com.ispan.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.dto.CityDTO;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.service.pet.VForService;

@CrossOrigin(origins = "http://localhost:8080") // 允許來自 Vue 前端的請求
@RestController
@RequestMapping("/api")
public class CityController {

    @Autowired
    private VForService vForService; // 引入 VForService

    // 返回CityDTO的列

    // @GetMapping("/cities")
    // public List<CityDTO> getCities() {
    //     return vForService.allCity();
    // }

    @GetMapping("/districts/{cityId}")
    public List<DistrictArea> getDistrictsByCity(@PathVariable Integer cityId) {
        return vForService.getDistrictsByCity(cityId); // 調用服務來查詢
    }

}
