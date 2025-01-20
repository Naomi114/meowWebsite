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

import jakarta.transaction.Transactional;
import tw.com.ispan.config.FileStorageProperties;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductImage;
import tw.com.ispan.dto.ProductImageRequest;
import tw.com.ispan.repository.shop.ProductImageRepository;

/*  圖片服務需實現以下功能：
    1. 儲存圖片至伺服器
    2. 儲存圖片相關數據至資料庫
    3. 推送到gitHub遠端儲存庫 (進階連接到 Azure SQL/BLOB Storage；待實現)
*/
@Service
@Transactional
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    public void addProductImages(Product product, List<ProductImageRequest> productImages) throws InterruptedException {
        for (ProductImageRequest imageRequest : productImages) {
            try {
                // 儲存圖片文件
                String imagePath = saveImageToStorage(imageRequest.getFilename());

                // 創建圖片實體
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imagePath);
                productImage.setIsPrimary(imageRequest.getIsPrimary()); // 第一張圖片設為主圖片
                productImage.setCreatedAt(LocalDateTime.now());
                productImageRepository.save(productImage);

                // 保存到商品圖片集合
                product.getProductImages().add(productImage);

                // 提交到 GitHub
                commitAndPushToGitHub("新增圖片: " + imageRequest.getFilename());

            } catch (IOException e) {
                throw new RuntimeException("圖片存儲失敗: " + e.getMessage());
            }
        }
    }

    private String saveImageToStorage(String fileName) throws IOException {
        Path uploadPath = fileStorageProperties.getValidatedUploadPath();

        // 驗證圖片名稱及格式
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("文件名不能為空");
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
