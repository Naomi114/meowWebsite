package tw.com.ispan.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.jwt.JsonWebTokenUtility;
import tw.com.ispan.service.MemberService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class LoginController {

    @Autowired
    private MemberService memberService; // 修正變數命名為小寫

    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @PostMapping("/ajax/secure/login")
    public String login(@RequestBody String entity) {
        JSONObject responseJson = new JSONObject();

        // 接收資料
        JSONObject obj = new JSONObject(entity);
        String email = obj.isNull("email") ? null : obj.getString("email");
        String password = obj.isNull("password") ? null : obj.getString("password");

        // 驗證資料
        if (email == null || email.length() == 0 || password == null || password.length() == 0) {
            responseJson.put("success", false);
            responseJson.put("message", "請輸入帳號/密碼");
            return responseJson.toString();
        }

        // 呼叫 Service 層的 login 方法，傳入 email 而不是 username
        Member bean = memberService.login(email, password);

        // 根據結果來決定回應
        if (bean == null) {
            responseJson.put("success", false);
            responseJson.put("message", "登入失敗");
            System.out.println("查無此會員: " + email);
        } else {
            responseJson.put("success", true);
            responseJson.put("message", "登入成功");

            JSONObject user = new JSONObject()
                    .put("memberId", bean.getMemberId()) // memberId //把小朱原本custid改成memberId
                    .put("email", bean.getEmail()) // email
                    .put("nickname", bean.getNickName()); // nickname

            // 生成 JWT token
            String token = jsonWebTokenUtility.createToken(user.toString());
            responseJson.put("token", token);
            responseJson.put("user", user);
        }

        return responseJson.toString();
    }
}
