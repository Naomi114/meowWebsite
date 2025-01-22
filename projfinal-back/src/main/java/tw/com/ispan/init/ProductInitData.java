package tw.com.ispan.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.CategoryRequest;
import tw.com.ispan.dto.CategoryResponse;
import tw.com.ispan.dto.ProductTagRequest;
import tw.com.ispan.dto.ProductTagResponse;
// import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.service.shop.CategoryService;
import tw.com.ispan.service.shop.ProductImageService;
import tw.com.ispan.service.shop.ProductTagService;

@Component
@Profile("dev") // 分離測試和開發環境
public class ProductInitData implements CommandLineRunner {

    // @Autowired
    // private AdminRepository adminRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private ProductImageService productImageService;

    @Transactional
    public void run(String... args) throws Exception {

        // 初始化管理員資料 (待整合Jude的部分)
        // initializeAdmin();

        // 初始化類別資料
        initializeCategories();

        // 初始化標籤資料
        initializeTags();

        // 初始化商品資料
        initializeData();
    }

    // 初始化管理員資料，不然Admin實體無法持久化匯入資料庫 (待整合Jude的部分)
    /*
     * JpaSpecificationExecutor 的方法如 findAll(Specification<T>) 和
     * count(Specification<T>) 是支持的，但 exists(Specification<T>) 不被內建支持。
     * 如果希望基於 Specification 檢查是否存在，可以通過 count 方法模擬：
     */
    // private void initializeAdmin() {
    // String adminName = "admin";
    // Admin admin = new Admin();
    // admin.setAdminName(adminName);
    // admin.setPassword("a123"); // 預設密碼
    // admin.setCreateDate(LocalDateTime.now());
    // admin.setUpdateDate(LocalDateTime.now());
    // adminRepository.save(admin);
    // System.out.println("新增管理員: " + adminName);
    // }

    private void initializeCategories() {
        List<CategoryInitData> categories = List.of(
                new CategoryInitData("寵物用品", "個", "所有寵物相關商品"),
                new CategoryInitData("玩具", "個", "各類寵物玩具"),
                new CategoryInitData("飼料", "包", "各種寵物飼料"),
                new CategoryInitData("保健品", "罐", "寵物專用保健產品"),
                new CategoryInitData("清潔用品", "包", "清潔與衛生用品"));

        categories.forEach(this::processCategory);
    }

    @Transactional
    private void processCategory(CategoryInitData data) {
        try {
            // 使用 CategoryService 進行新增或更新
            CategoryRequest request = new CategoryRequest();
            request.setCategoryName(data.getCategoryName());
            request.setCategoryDescription(data.getCategoryDescription());
            request.setUnit(data.getDefaultUnit());

            CategoryResponse response = categoryService.createOrUpdateCategory(request);

            // 記錄初始化結果
            if (response.getSuccess()) {
                System.out.println("初始化類別成功: " + response.getCategoryName());
            } else {
                System.err.println("初始化類別失敗: " + response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("初始化類別時發生錯誤: " + data.getCategoryName());
            e.printStackTrace();
            // 重要：拋出異常以便 Spring 正確處理事務
            throw e;
        }
    }

    private void initializeTags() {
        List<ProductTagRequest> tags = List.of(
                new ProductTagRequest("純天然", "初始化標籤: 純天然"),
                new ProductTagRequest("純手作", "初始化標籤: 純手作"),
                new ProductTagRequest("低敏", "初始化標籤: 低敏"),
                new ProductTagRequest("無添加", "初始化標籤: 無添加"));

        tags.forEach(tagRequest -> {
            ProductTagResponse response = productTagService.createTag(tagRequest);
            if (response.getSuccess()) {
                System.out.println("成功初始化標籤: " + tagRequest.getTagName());
            } else {
                System.err.println("初始化標籤失敗: " + response.getMessage());
            }
        });
    }

    // 初始化五組商品假資料
    public void initializeData() {

        try {
            CategoryResponse categoryResponse1 = categoryService.findCategory("飼料");

            Product product1 = new Product();
            product1.setProductName("貓糧");
            product1.setDescription("優質貓糧");
            product1.setUnit(categoryResponse1.getDefaultUnit());
            product1.setOriginalPrice(BigDecimal.valueOf(20.00));
            product1.setSalePrice(BigDecimal.valueOf(18.00));
            product1.setStockQuantity(100);
            product1.setStatus("上架中");
            product1.setExpire(LocalDate.parse("2025-12-31"));
            product1.setCreatedAt(LocalDateTime.now());
            product1.setUpdatedAt(LocalDateTime.now());

            // 提供文件名列表
            List<String> filenames1 = List.of("image11.jpg", "image12.jpg");
            try {
                productImageService.addProductImages(product1, filenames1);
            } catch (Exception e) {
                System.err.println("圖片初始化失敗: " + e.getMessage());
            }

            // product1.setAdmin(new Admin(1, "管理員1"));
            /*
             * 假設 adminId 1 是 "管理員1"；
             * 手動創建的實體處於分離狀態（detached），不能直接關聯到其他實體。
             * 還是需要從adminRepository中查找對應的管理員實體。
             */
            CategoryResponse categoryResponse2 = categoryService.findCategory("寵物用品");

            Product product2 = new Product();
            product2.setProductName("狗項圈");
            product2.setDescription("耐用的皮革狗項圈");
            product2.setUnit(categoryResponse2.getDefaultUnit());
            product2.setOriginalPrice(BigDecimal.valueOf(15.00));
            product2.setSalePrice(BigDecimal.valueOf(12.50));
            product2.setStockQuantity(200);
            product2.setStatus("上架中");
            product2.setExpire(LocalDate.parse("2030-12-31"));
            product2.setCreatedAt(LocalDateTime.now());
            product2.setUpdatedAt(LocalDateTime.now());
            List<String> filenames2 = List.of("image2.jpg");
            try {
                productImageService.addProductImages(product2, filenames2);
            } catch (Exception e) {
                System.err.println("圖片初始化失敗: " + e.getMessage());
            }
            // product2.setAdmin(new Admin(2, "管理員2"));

            CategoryResponse categoryResponse3 = categoryService.findCategory("玩具");

            Product product3 = new Product();
            product3.setProductName("貓抓板");
            product3.setDescription("耐用的貓抓板");
            product3.setUnit(categoryResponse3.getDefaultUnit());
            product3.setOriginalPrice(BigDecimal.valueOf(10.00));
            product3.setSalePrice(BigDecimal.valueOf(8.00));
            product3.setStockQuantity(150);
            product3.setStatus("上架中");
            product3.setExpire(LocalDate.parse("2025-12-31"));
            product3.setCreatedAt(LocalDateTime.now());
            product3.setUpdatedAt(LocalDateTime.now());
            List<String> filenames3 = List.of("image3.jpg");
            try {
                productImageService.addProductImages(product3, filenames3);
            } catch (Exception e) {
                System.err.println("圖片初始化失敗: " + e.getMessage());
            }
            // product3.setAdmin(new Admin(1, "管理員1"));

            CategoryResponse categoryResponse4 = categoryService.findCategory("寵物用品");

            Product product4 = new Product();
            product4.setProductName("狗玩具");
            product4.setDescription("耐用的狗玩具");
            product4.setUnit(categoryResponse4.getDefaultUnit());
            product4.setOriginalPrice(BigDecimal.valueOf(5.00));
            product4.setSalePrice(BigDecimal.valueOf(4.00));
            product4.setStockQuantity(300);
            product4.setStatus("上架中");
            product4.setExpire(LocalDate.now());
            product4.setCreatedAt(LocalDateTime.now());
            product4.setUpdatedAt(LocalDateTime.now());
            List<String> filenames4 = List.of("image41.jpg", "image42.jpg", "image43.jpg");
            try {
                productImageService.addProductImages(product4, filenames4);
            } catch (Exception e) {
                System.err.println("圖片初始化失敗: " + e.getMessage());
            }
            // product4.setAdmin(new Admin(2, "管理員2"));

            CategoryResponse categoryResponse5 = categoryService.findCategory("清潔用品");

            Product product5 = new Product();
            product5.setProductName("貓砂");
            product5.setDescription("高效吸水貓砂");
            product5.setUnit(categoryResponse5.getDefaultUnit());
            product5.setOriginalPrice(BigDecimal.valueOf(25.00));
            product5.setSalePrice(BigDecimal.valueOf(22.00));
            product5.setStockQuantity(80);
            product5.setStatus("上架中");
            product5.setExpire(LocalDate.parse("2025-06-30"));
            product5.setCreatedAt(LocalDateTime.now());
            product5.setUpdatedAt(LocalDateTime.now());
            List<String> filenames5 = List.of("image5.png");
            try {
                productImageService.addProductImages(product5, filenames5);
            } catch (Exception e) {
                System.err.println("圖片初始化失敗: " + e.getMessage());
            }
            // product5.setAdmin(new Admin(1, "管理員1"));

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);

            System.out.println("初始化商城資料成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
