package tw.com.ispan.controller.pet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tw.com.ispan.service.pet.ImageService;

//三個案件共用的單個圖片上傳
@RestController
@RequestMapping(path = { "/api/Case" })
public class UploadImageController {

	@Autowired
	private ImageService imageService;

	// 暫存路徑
	@Value("${file.tmp-upload-dir}") // 存到外部資料夾 C:/upload/tmp/pet/images/
	private String tmpUploadDir;

	// 修改、新增案件的圖檔上傳都用這個
	// 圖片上傳先進到暫存資料夾upload/tmp/pet/images，並返回url給前端，等按下新增案件，將圖片移轉到永存資料夾(新url)，並將案件和圖片數據交給新增案件controller
	// 前端頁面只會有三個上傳按鈕，藉此限制只能上傳三張
	@PostMapping("/uploadImage")
	public ResponseEntity<Map<String, String>> uploadImage(@RequestHeader("Authorization") String token,
			@RequestPart("file") MultipartFile file) throws IOException {

		Map<String, String> response = new HashMap<>();

		// 1. 驗證token

		// 2. 驗證圖檔格式
		if (file.isEmpty()) {
			response.put("status", "400");
			response.put("message", "檔案不得為空");
			return ResponseEntity.badRequest().body(response); // spring內建ResponseEntity類別，可創建http回應
		}
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) { // image開頭包含image/jpeg、image/png、image/gif
			response.put("status", "400");
			response.put("message", "檔案格式只能為圖片");
			return ResponseEntity.badRequest().body(response);
		}

		// 3.存到暫存資料夾(路徑或URL返回到前端，作為一個標識，接著再從前端傳給後端新增案件controller使用)
		// 將上傳的文件內容保存到指定暫存路徑
		Map<String, String> fileMessage = imageService.tmpSaveImage(file);

		if (fileMessage != null) {
			// 上傳成功(返回tempUrl給前端使用，等送出案件再把圖片永存資料夾url存到資料庫中)
			response.put("fileName", fileMessage.get("fileName"));
			response.put("frontTmpUrl", fileMessage.get("frontTmpUrl"));
			response.put("backTmpUrl", fileMessage.get("backTmpUrl"));
			response.put("status", fileMessage.get("status"));
			response.put("message", fileMessage.get("message"));
			return ResponseEntity.ok(response);
		} else {
			// 上傳失敗回傳
			response.put("status", "500");
			response.put("message", "圖片上傳失敗");
			return ResponseEntity.badRequest().body(response);
		}

	}

}
