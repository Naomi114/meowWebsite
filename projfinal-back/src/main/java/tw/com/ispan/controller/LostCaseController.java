package tw.com.ispan.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.dto.LostCaseResponse;
import tw.com.ispan.service.pet.LostCaseService;

@RestController
@RequestMapping("/lostcases")
public class LostCaseController {
    @Autowired
    private LostCaseService lostCaseService;

    @PostMapping
    public LostCaseResponse create(@RequestBody String json) {
        LostCaseResponse responseBean = new LostCaseResponse();

        JSONObject obj = new JSONObject(json);
        Integer id = obj.isNull("memberId") ? null : obj.getInt("memberId");

        if (id == null) {
            responseBean.setSuccess(false);
            responseBean.setMessage("id是必要欄位");
        } else if (lostCaseService.exists(id)) {
            responseBean.setSuccess(false);
            responseBean.setMessage("id已存在");
        } else {
            LostCase insert = lostCaseService.create(json);
            if (insert == null) {
                responseBean.setSuccess(false);
                responseBean.setMessage("新增失敗");
            } else {
                responseBean.setSuccess(true);
                responseBean.setMessage("新增成功");
            }
        }
        return responseBean;
    }

    @PutMapping("/{id}")
    public String modify(@PathVariable Integer id, @RequestBody String entity) {
        JSONObject responseJson = new JSONObject();
        if (id == null) {
            responseJson.put("success", false);
            responseJson.put("message", "Id是必要欄位");
        } else if (!lostCaseService.exists(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "Id不存在");
        } else {
            LostCase update = lostCaseService.modify(entity);
            if (update == null) {
                responseJson.put("success", false);
                responseJson.put("message", "修改失敗");
            } else {
                responseJson.put("success", true);
                responseJson.put("message", "修改成功");
            }
        }
        return responseJson.toString();
    }

    @DeleteMapping("/{pk}")
    public String remove(@PathVariable("pk") Integer id) {
        JSONObject responseJson = new JSONObject();

        if (id == null) {
            responseJson.put("success", false);
            responseJson.put("message", "id是必要欄位");
            return responseJson.toString();
        } else if (!lostCaseService.exists(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "id不存在");
            return responseJson.toString();
        }

        boolean delete = lostCaseService.remove(id);
        if (!delete) {
            responseJson.put("success", false);
            responseJson.put("message", "刪除失敗");
        } else {
            responseJson.put("success", true);
            responseJson.put("message", "刪除成功");
        }

        return responseJson.toString();
    }

    @GetMapping("/{id}")
    public String findByPrimaryKey(@PathVariable(name = "id") Integer id) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();

        if (id != null) {
            LostCase lostCase = lostCaseService.findById(id); // 调用 service 层方法查询 LostCase
            if (lostCase != null) {
                JSONObject item = new JSONObject()
                        .put("lostCaseId", lostCase.getLostCaseId())
                        .put("caseTitle", lostCase.getCaseTitle())
                        .put("species", lostCase.getSpecies() != null ? lostCase.getSpecies().getSpecies() : null)
                        .put("breed", lostCase.getBreed() != null ? lostCase.getBreed().getBreedId() : null)
                        .put("furColor", lostCase.getFurColor() != null ? lostCase.getFurColor().getFurColorId() : null)
                        .put("gender", lostCase.getGender())
                        .put("age", lostCase.getAge())
                        .put("microChipNumber", lostCase.getMicroChipNumber())
                        .put("suspLost", lostCase.isSuspLost())
                        .put("city", lostCase.getCity() != null ? lostCase.getCity().getCityId() : null)
                        .put("distinctArea",
                                lostCase.getDistinctArea() != null ? lostCase.getDistinctArea().getDistinctAreaId()
                                        : null)
                        .put("street", lostCase.getStreet())
                        .put("latitude", lostCase.getLatitude())
                        .put("longitude", lostCase.getLongitude())
                        .put("donationAmount", lostCase.getDonationAmount())
                        .put("viewCount", lostCase.getViewCount())
                        .put("follow", lostCase.getFollow())
                        .put("publicationTime",
                                lostCase.getPublicationTime() != null ? lostCase.getPublicationTime().toString() : null)
                        .put("lastUpdateTime",
                                lostCase.getLastUpdateTime() != null ? lostCase.getLastUpdateTime().toString() : null)
                        .put("lostExperience", lostCase.getLostExperience())
                        .put("contactInformation", lostCase.getContactInformation())
                        .put("featureDescription", lostCase.getFeatureDescription())
                        .put("caseState",
                                lostCase.getCaseState() != null ? lostCase.getCaseState().getCaseStateId() : null)
                        .put("caseUrl", lostCase.getCaseUrl());
                array.put(item); // 将数据添加到 JSON 数组中
            }
        }

        responseJson.put("list", array); // 将 JSON 数组封装到 JSON 对象中
        return responseJson.toString(); // 返回 JSON 字符串
    }

    @PostMapping("/find")
    public LostCaseResponse find(@RequestBody String json) {
        LostCaseResponse responseBean = new LostCaseResponse();

        long count = lostCaseService.count(json);
        responseBean.setCount(count);

        List<LostCase> products = lostCaseService.find(json);
        if (products != null && !products.isEmpty()) {
            responseBean.setList(products);
        } else {
            responseBean.setList(new ArrayList<>());
        }

        return responseBean;
    }
}
