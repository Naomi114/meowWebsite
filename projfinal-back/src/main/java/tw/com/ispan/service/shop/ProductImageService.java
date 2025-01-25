package tw.com.ispan.service.shop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import tw.com.ispan.config.FileStorageProperties;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductImage;
import tw.com.ispan.dto.ProductImageRequest;
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

    // 初始化圖片資料
    public void initializeProductImages(Product product, List<MultipartFile> filenames) {
        try {
            // 將文件名列表轉換為 ProductImageRequest 列表
            filenames.stream()
                    .map(filename -> {
                        ProductImageRequest request = new ProductImageRequest();
                        request.setFilename(List.of(filename));
                        // 第一張圖片設為主圖片
                        request.setIsPrimary(filenames.indexOf(filename) == 0);
                        return request;
                    })
                    .toList();

            // 調用 processProductImage 方法處理圖片
            processProductImage(product, filenames);

            System.out.println("圖片初始化成功");
        } catch (Exception e) {
            System.err.println("圖片初始化失敗: " + e.getMessage());
        }
    }

    // 
    public void processProductImage(Product product, List<MultipartFile> filenames) {
        if (filenames == null || filenames.isEmpty()) {
            throw new IllegalArgumentException("商品圖片不能為空");
        }

        // 檢查圖片數量
        if (filenames.size() < 1 || filenames.size() > 5) {
            throw new IllegalArgumentException("商品圖片數量必須在 1 到 5 之間");
        }

        for (MultipartFile imageRequest : filenames) {
            try {
                // 儲存圖片到存儲系統
                String imageUrl = saveImageToStorage(imageRequest.getOriginalFilename());

                // 創建 ProductImage 實體
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageUrl);
                // Assuming the first image in the list is the primary image
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

    private String saveImageToStorage(String fileName) throws IOException {
        Path uploadPath = fileStorageProperties.getValidatedUploadPath();

        // 驗證圖片名稱及格式
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("至少提供一個圖片檔");
        }

        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            throw new IOException("僅支持 JPG 和 PNG 格式的圖片");
        }

        // 生成唯一文件名
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, ("Image content for " + fileName).getBytes());

        // 使用配置文件中的 base-url
        return fileStorageProperties.getBaseUrl() + "/" + uniqueFileName;

        // 動態生成 URL (是否更好用? 待測)
        // return ServletUriComponentsBuilder.fromCurrentContextPath()
        // .path("/images/")
        // .path(uniqueFileName)
        // .toUriString();
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
