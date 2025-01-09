package tw.com.ispan.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.CustomerBean;
import tw.com.ispan.jwt.JsonWebTokenUtility;
import tw.com.ispan.service.CustomerService;

@RestController
public class LoginAjacController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @PostMapping("/ajax/secure/login")
    public String login(@RequestBody String entity) {
        JSONObject responseJson = new JSONObject();

        // 接收資料
        JSONObject obj = new JSONObject(entity);
        String username = obj.isNull("username") ? null : obj.getString("username");
        String password = obj.isNull("password") ? null : obj.getString("password");

        // 驗證資料
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            responseJson.put("success", false);
            responseJson.put("message", "請輸入帳號/密碼");
            return responseJson.toString();
        }

        // 呼叫Model
        CustomerBean bean = customerService.login(username, password);

        // 根據Model執行結果決定要呼叫的View
        if (bean == null) {
            responseJson.put("success", false);
            responseJson.put("message", "登入失敗");
        } else {
            responseJson.put("success", true);
            responseJson.put("message", "登入成功");

            JSONObject user = new JSONObject()
                    .put("custid", bean.getCustid())
                    .put("email", bean.getEmail());
            String token = jsonWebTokenUtility.createToken(user.toString());
            responseJson.put("token", token);
            responseJson.put("user", bean.getEmail());
        }
        return responseJson.toString();
    }
}
