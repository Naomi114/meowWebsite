package tw.com.ispan.controller.pet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.dto.shop.ReportCaseResponse;
import tw.com.ispan.service.pet.ReportCaseService;

@RestController
@CrossOrigin
@RequestMapping("/reports")
public class ReportCaseController {

    @Autowired
    private ReportCaseService reportCaseService;

    @PostMapping
    public ResponseEntity<String> createReportCase(@RequestBody String requestBody) {
        JSONObject responseJson = new JSONObject();

        try {
            // 调用 Service 的 create 方法
            ReportCase newReportCase = reportCaseService.create(requestBody);

            // 構建成功響應
            responseJson.put("success", true);
            responseJson.put("message", "舉報案件創建成功！");
            responseJson.put("data", new JSONObject()
                    .put("reportCaseId", newReportCase.getReportId())
                    .put("reportTitle", newReportCase.getReportTitle())
                    .put("reportDate", newReportCase.getReportDate().toString())
                    .put("reportNotes", newReportCase.getReportNotes())
                    .put("reportState", newReportCase.isReportState()));
            return ResponseEntity.ok(responseJson.toString());

        } catch (IllegalArgumentException e) {
            // 處理非法參數異常
            responseJson.put("success", false);
            responseJson.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseJson.toString());

        } catch (Exception e) {
            // 處理其他異常
            responseJson.put("success", false);
            responseJson.put("message", "舉報案件創建失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson.toString());
        }
    }

    @PutMapping("/review/{Id}")
    public ResponseEntity<String> reviewReport(
            @PathVariable Integer reportCaseId,
            @RequestParam Integer adminId,
            @RequestParam boolean isApproved,
            @RequestParam boolean hideCase) {

        JSONObject responseJson = new JSONObject();

        try {
            // 调用服务层的 review 方法进行处理
            ReportCase updatedReportCase = reportCaseService.review(reportCaseId, adminId, isApproved, hideCase);

            // 构建成功的响应
            responseJson.put("success", true);
            responseJson.put("message", "審核成功！");
            responseJson.put("data", new JSONObject()
                    .put("reportCaseId", updatedReportCase.getReportId())
                    .put("isApproved", updatedReportCase.isReportState())
                    .put("adminId", adminId)
                    .put("hideCase", hideCase));
            return ResponseEntity.ok(responseJson.toString());

        } catch (IllegalArgumentException e) {
            // 参数无效的错误处理
            responseJson.put("success", false);
            responseJson.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseJson.toString());
        } catch (Exception e) {
            // 其他异常的错误处理
            responseJson.put("success", false);
            responseJson.put("message", "審核失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson.toString());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifyReportCase(@RequestBody String requestBody) {
        JSONObject responseJson = new JSONObject();

        try {
            // 调用 Service 的 modify 方法，传递请求体中的 JSON
            ReportCase updatedReportCase = reportCaseService.modify(requestBody);

            // 若成功，返回修改後的報告信息
            if (updatedReportCase != null) {
                responseJson.put("success", true);
                responseJson.put("message", "報告案件修改成功！");
                responseJson.put("data", new JSONObject()
                        .put("reportCaseId", updatedReportCase.getReportId())
                        .put("reportTitle", updatedReportCase.getReportTitle())
                        .put("reportDate", updatedReportCase.getReportDate().toString())
                        .put("reportNotes", updatedReportCase.getReportNotes())
                        .put("reportState", updatedReportCase.isReportState())
                        .put("updateDate", updatedReportCase.getUpdateDate().toString()));
                return ResponseEntity.ok(responseJson.toString());
            } else {
                responseJson.put("success", false);
                responseJson.put("message", "修改失敗！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson.toString());
            }

        } catch (IllegalArgumentException e) {
            // 處理非法參數異常
            responseJson.put("success", false);
            responseJson.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseJson.toString());

        } catch (Exception e) {
            // 處理其他異常
            responseJson.put("success", false);
            responseJson.put("message", "修改報告失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJson.toString());
        }
    }

    @PostMapping("/find")
    public ReportCaseResponse find(@RequestBody String json) {
        ReportCaseResponse responseBean = new ReportCaseResponse();

        long count = reportCaseService.count(json);
        responseBean.setCount(count);

        List<ReportCase> products = reportCaseService.find(json);
        if (products != null && !products.isEmpty()) {
            responseBean.setList(products);
        } else {
            responseBean.setList(new ArrayList<>());
        }

        return responseBean;
    }
}
