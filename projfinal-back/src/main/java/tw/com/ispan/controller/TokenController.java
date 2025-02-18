package tw.com.ispan.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

//冠儒設置
@CrossOrigin
@RestController
public class TokenController {

    @PostMapping("/api/validateToken")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestAttribute("memberId") Integer memberId) {

        // 已將此方法塞入攔截驗證，就會自行做token驗證
        System.out.println("目前是memberId進入網頁: " + memberId);

        Map<String, Object> response = new HashMap<>();

        if (memberId != null) {
            response.put("valid", true);
            response.put("memberId", memberId);
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("message", "Token is invalid or expired");
            return ResponseEntity.status(403).body(response);
        }
    }
}
