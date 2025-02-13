package tw.com.ispan.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/*路徑驗證與初始化*/

@Component
@ConfigurationProperties(prefix = "file.storage") // 適合多個屬性讀取；對應yml檔中的路徑
public class FileStorageProperties {
    private String uploadDir;
    private String baseUrl; // 基礎 URL 字段

    public String getUploadDir() {
        System.out.println("文件存儲路徑: " + uploadDir);
        if (uploadDir == null || uploadDir.isBlank()) {
            throw new IllegalStateException("文件存儲路徑未正確配置，請檢查 application.yml");
        }
        return uploadDir;
    }

    public Path getValidatedUploadPath() {
        Path uploadPath = Paths.get(getUploadDir());
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("無法創建文件存儲目錄: " + e.getMessage());
            }
        }
        return uploadPath;
    }

    @PostConstruct
    public void validateAndInitialize() {
        try {
            System.out.println("檢查文件存儲配置...");
            Path uploadPath = getValidatedUploadPath(); // 確保初始化時檢查並創建目錄
            System.out.println("文件存儲路徑檢查完成: " + uploadPath.toAbsolutePath());
        } catch (IllegalStateException e) {
            System.err.println("文件存儲路徑配置錯誤: " + e.getMessage());
            throw e; // 繼續拋出以中止應用啟動
        } catch (RuntimeException e) {
            System.err.println("文件存儲目錄初始化失敗: " + e.getMessage());
            throw e;
        }
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
