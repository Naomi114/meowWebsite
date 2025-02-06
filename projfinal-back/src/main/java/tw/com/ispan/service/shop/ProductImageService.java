package tw.com.ispan.service.shop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import tw.com.ispan.config.FileStorageProperties;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductImage;
import tw.com.ispan.repository.shop.ProductImageRepository;

/*  圖片服務需實現以下功能：
    1. 檢查圖片數量是否為 1~5 張
	2. 驗證每張圖片的內容
    3. 儲存圖片相關數據至資料庫
    4. 推送到gitHub遠端儲存庫 (進階連接到 Azure SQL/BLOB Storage；待實現)
*/
@Service
@Transactional
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    // 初始化：從本地讀取圖片並儲存 URL
    public void initializeProductImages(Product product, List<String> filenames) {
        List<MultipartFile> multipartFiles = getMultipartFilesByNames(filenames);
        processProductImage(product, multipartFiles);
    }

    // 上線後：處理使用者上傳的圖片
    public String saveUploadedFile(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = fileStorageProperties.getValidatedUploadPath().resolve(uniqueFileName);

        // 儲存圖片
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 回傳圖片的 URL
        return fileStorageProperties.getBaseUrl() + "/" + uniqueFileName;
    }

    // 透過圖片名稱列表，轉換成 `MultipartFile`
    private List<MultipartFile> getMultipartFilesByNames(List<String> filenames) {
        Path uploadPath = fileStorageProperties.getValidatedUploadPath();
        return filenames.stream()
                .map(name -> uploadPath.resolve(name).toFile())
                .filter(File::exists)
                .map(this::convertFileToMultipartFile)
                .collect(Collectors.toList());
    }

    // 讀取本地檔案並轉換為 `MultipartFile`
    private MultipartFile convertFileToMultipartFile(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            return new MockMultipartFile(
                    file.getName(), file.getName(),
                    "image/" + getFileExtension(file.getName()),
                    IOUtils.toByteArray(input)
            );
        } catch (IOException e) {
            throw new RuntimeException("無法讀取圖片檔案: " + file.getAbsolutePath(), e);
        }
    }

    //驗證&取得檔案副檔名
    private String getFileExtension(String fileName) throws IOException{
        // 驗證圖片名稱及格式
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("至少提供一個圖片檔");
        }

        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            throw new IOException("僅支持 JPG 和 PNG 格式的圖片");
        }
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex);
    }

    public void processProductImage(Product product, List<MultipartFile> filenames) {
        if (filenames == null || filenames.isEmpty()) {
            throw new IllegalArgumentException("商品圖片不能為空");
        }

        // 檢查圖片數量
        if (filenames.size() < 1 || filenames.size() > 5) {
            throw new IllegalArgumentException("商品圖片數量必須在 1 到 5 之間");
        }

        for (MultipartFile imagefFile : filenames) {
            try {
                // 儲存圖片到存儲系統
                String imageUrl = saveImageToStorage(imagefFile);

                // 創建 ProductImage 實體
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageUrl);
                productImage.setIsPrimary(product.getProductImages().isEmpty());
                productImage.setCreatedAt(LocalDateTime.now());
                productImage.setProduct(product);

                // 保存圖片數據到資料庫
                productImageRepository.save(productImage);

                // 添加到商品的圖片集合
                product.getProductImages().add(productImage);

                // 提交到 GitHub (測試階段先註解，上線再解開)
                // commitAndPushToGitHub("新增圖片: " + imageRequest.getFilename());

            } catch (IOException e) {
                throw new RuntimeException("圖片存儲失敗: " + e.getMessage());
            }
        }
    }

    private String saveImageToStorage(MultipartFile file) throws IOException {
        // 生成唯一文件名
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = fileStorageProperties.getValidatedUploadPath().resolve(uniqueFileName);

        // 儲存圖片
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileStorageProperties.getBaseUrl() + "/" + uniqueFileName;

    }

    private void commitAndPushToGitHub(String commitMessage) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File(fileStorageProperties.getUploadDir()));

        builder.command("git", "add", ".").start().waitFor();
        builder.command("git", "commit", "-m", commitMessage).start().waitFor();
        builder.command("git", "push", "origin", "main").start().waitFor();

        System.out.println("文件已提交並推送到 GitHub");
    }
}
