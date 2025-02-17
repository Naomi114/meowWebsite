package tw.com.ispan.service.pet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.repository.pet.CasePictureRepository;

@Service
@Transactional
public class ImageAdoptService {

    @Autowired
    private CasePictureRepository casePictureRepository;

    @Value("${back.domainName.url}") // http://localhost:8080
    private String domainName;

    // 後端暫存路徑
    @Value("${file.tmp-upload-dir}")
    private String tmpUploadDir;

    // 後端永存路徑
    @Value("${file.final-upload-dir}")
    private String finalUploadDir;

    // 將圖片暫存於暫存資料夾
    public Map<String, String> tmpSaveImage(MultipartFile file) {

        // 組裝返回訊息(檔名、路徑)
        Map<String, String> fileMessage = new HashMap<>();

        Path tmpPath = Paths.get(tmpUploadDir);

        // 也可使用操作系統的臨時目錄，如果臨時文件只需要短期存儲
        // String tmpDir = System.getProperty("java.io.tmpdir");
        // Path tmpUploadPath = Paths.get(tmpDir, "upload/tmp/pet/images");

        // 生成唯一文件名，防止文件名衝突
        // (圖片名預計取為memberid_caseid，但須要從token抓會員資料才能抓)------------------------------------------
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 如果暫存目錄不存在則先創建目錄
        if (!Files.exists(tmpPath)) {
            try {
                Files.createDirectories(tmpPath);
            } catch (IOException e) {
                System.out.println("路徑錯誤");
                e.printStackTrace();
            }
        }

        // 將文件路徑組合起來---------------------------------------------------------------------------------
        Path filePath = tmpPath.resolve(fileName);

        // 將上傳的文件內容保存到指定路徑
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("檔案路徑不存在，上傳失敗");
            e.printStackTrace();
        }

        // 最後檢查文件是否順利存儲
        if (!Files.exists(filePath)) {
            System.out.println("文件存儲失敗");
            return null;
        }

        // 3. **回傳給前端可訪問的 URL**
        String tempUrl = domainName + "/upload/tmp/pet/images/" + fileName;
        System.out.println("返回給前端的路徑" + tempUrl);

        // 上傳成功則返回檔案相關訊息
        fileMessage.put("fileName", fileName);
        fileMessage.put("frontTmpUrl", tempUrl);
        fileMessage.put("backTmpUrl", filePath.toString());
        fileMessage.put("status", "200");
        fileMessage.put("message", "圖片上傳成功，後端專案中路徑：" + filePath.toString());
        return fileMessage;
    }

    // 將暫存資料夾中圖片移到永存資料夾------------------------------------------------------------------------------------
    public List<String> moveImages(List<String> tmpUrls) {

        // 用來保存新圖片路徑
        List<String> finalUrl = new ArrayList<String>();

        // step1 先把圖片一個個從暫存資料夾移到永存資料夾，並返回新圖片路徑
        for (String tmpUrl : tmpUrls) {

            // 定義來源檔案路徑
            Path sourcePath = Paths.get(tmpUrl);

            // 定義目標檔案路徑（包括目標檔案名稱）
            // 先從路徑中擷取檔案名稱，再將之與轉為專案執行環境絕對路徑的字串拼接，獲得最終目標檔案路徑
            String fileName = sourcePath.getFileName().toString();
            Path absolutePath = Paths.get(finalUploadDir);
            Path targetPath = absolutePath.resolve(fileName);

            try {
                // 確保目標目錄存在，否則創建 .getParent()返回檔案目錄而非文件本身
                if (!Files.exists(targetPath.getParent())) {
                    Files.createDirectories(targetPath.getParent());
                }
                // 移動檔案
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("檔案已成功移動到：" + targetPath);

                finalUrl.add(targetPath.toString().replace("\\", "/")); // 確保使用 `/`

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("檔案移動失敗：" + e.getMessage());
            }
        }
        System.out.println(finalUrl.toString());
        return finalUrl;
    }

}
