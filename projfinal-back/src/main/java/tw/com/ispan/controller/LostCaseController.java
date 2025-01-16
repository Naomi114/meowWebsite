package tw.com.ispan.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.dto.LostCaseResponse;
import tw.com.ispan.service.pet.LostCaseService;
import tw.com.ispan.util.DatetimeConverter;

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
        } else if (!lostCaseService.exists(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "id不存在");
        } else {
            boolean delete = lostCaseService.remove(id);
            if (!delete) {
                responseJson.put("success", false);
                responseJson.put("message", "刪除失敗");
            } else {
                responseJson.put("success", true);
                responseJson.put("message", "刪除成功");
            }
        }
        return responseJson.toString();
    }

    @GetMapping("/{id}")
    public String findByPrimaryKey(@PathVariable Integer id) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        if (id != null) {
            LostCase findCase = lostCaseService.findById(id);
            if (findCase != null) {
                String date = DatetimeConverter.toString(findCase.getPublicationTime(), "yyyy-MM-dd");
                JSONObject item = new JSONObject()
                        .put("caseTitle", findCase.getCaseTitle())
                        .put("species", findCase.getSpecies())
                        .put("name", findCase.getName())
                        .put("gender", findCase.getGender())
                        .put("breed", findCase.getBreed())
                        .put("sterilization", findCase.getSterilization())
                        // .put("memberId", findCase.getMemberId())
                        .put("lastUpdateTime", date);
                array = array.put(item);
            }
        }
        responseJson = responseJson.put("list", array);
        return responseJson.toString();
    }

    // @PostMapping("/find")

    /**
     * 動態條件查詢 LostCase
     *
     * @param caseTitle        案件標題模糊查詢條件
     * @param memberIdPattern  memberId 模糊查詢條件
     * @param caseIdPattern    caseId 模糊查詢條件
     * @param cityName         城市名稱模糊查詢條件
     * @param distinctAreaName 鄉鎮區名稱模糊查詢條件
     * @return 符合條件的 LostCase 列表
     */
    @GetMapping("/search")
    public List<LostCase> searchCases(
            @RequestParam(required = false) String caseTitle,
            @RequestParam(required = false) String memberIdPattern,
            @RequestParam(required = false) String caseIdPattern,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String distinctAreaName) {

        return lostCaseService.findCases(caseTitle, memberIdPattern, caseIdPattern, cityName, distinctAreaName);
    }
}
