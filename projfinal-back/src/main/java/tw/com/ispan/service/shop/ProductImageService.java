package tw.com.ispan.service.shop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
*/
@Service
@Transactional
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    public void addProductImages(Product product, List<ProductImageRequest> productImages) {
        for (int i = 0; i < productImages.size(); i++) {
            ProductImageRequest imageRequest = productImages.get(i);

            try {
                // 儲存圖片文件
                String imagePath = saveImageToStorage(imageRequest.getImageUrl());

                // 創建圖片實體
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imagePath);
                productImage.setProduct(product);
                productImage.setIsPrimary(i == 0); // 第一張圖片設為主圖片
                productImage.setCreatedAt(LocalDateTime.now());
                // 儲存到資料庫 (後續擴展性更加)
                productImageRepository.save(productImage);

                // 保存到商品圖片集合
                product.getProductImages().add(productImage);

            } catch (IOException e) {
                throw new RuntimeException("圖片存儲失敗: " + e.getMessage());
            }
        }
    }

    private String saveImageToStorage(String fileName) throws IOException {
        String uploadDir = fileStorageProperties.getUploadDir();
        Path uploadPath = Paths.get(uploadDir);

        // 確保目錄存在
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path filePath = uploadPath.resolve(uniqueFileName);

        // 模擬圖片存儲
        Files.write(filePath, ("Image content for " + fileName).getBytes());
        return filePath.toString();
    }

}
