package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.com.ispan.util.EcpayFunctions;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Controller
@RequestMapping("/pages/ecpay")
public class EcpayController {

	@Autowired
	private EcpayFunctions ecpayFunctions;

	@PostMapping("/return")
	public String ecpayReturn(@RequestBody String body) {
		System.out.println("ecpay return " + System.currentTimeMillis());
		System.out.println("body=" + body);

		return "";
	}

	@PostMapping("/send")
	@ResponseBody
	@CrossOrigin
	public String send(@RequestBody Map<String, Object> body) {
		// 將 Map<String, Object> 轉換為 JSON 字串
		ObjectMapper objectMapper = new ObjectMapper();
		String bodyJson = null;
		try {
			bodyJson = objectMapper.writeValueAsString(body);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 呼叫 buildEcpayForm 並傳遞 JSON 字串
		String form = ecpayFunctions.buildEcpayForm(bodyJson);

		return form;
	}
}
