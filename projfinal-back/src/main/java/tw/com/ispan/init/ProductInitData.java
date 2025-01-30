package tw.com.ispan.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.CategoryRequest;
import tw.com.ispan.dto.CategoryResponse;
import tw.com.ispan.dto.ProductTagRequest;
import tw.com.ispan.dto.ProductTagResponse;
import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.service.shop.CategoryService;
import tw.com.ispan.service.shop.ProductImageService;
import tw.com.ispan.service.shop.ProductTagService;

@Component
@Order(2)
@Profile("dev") // 分離測試和開發環境
public class ProductInitData implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

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
        // 初始化管理員資料
        initializeAdmins();

        // 初始化類別資料
        initializeCategories();

        // 初始化標籤資料
        initializeTags();

        // 初始化商品資料
        initializeData();
    }

    private void initializeAdmins() {
        Admin admin = new Admin();
        admin.setAdminName("admin");
        admin.setPassword("AAA");
        admin.setCreateDate(LocalDateTime.now());
        admin.setUpdateDate(LocalDateTime.now());

        // 檢查是否已存在相同的 admin (by Naomi)
        String adminTmp = admin.getAdminName();
        if (adminRepository.findByAdminName(adminTmp) != null) {
            System.out.println("adminName 已存在: " + adminTmp);
        } else {
            adminRepository.save(admin);
        }
        System.out.println("商城初始化管理員成功！");
    }

    private void initializeCategories() {
        Set<CategoryRequest> categories = Set.of(
                new CategoryRequest("寵物用品", "所有寵物相關商品","個"),
                new CategoryRequest("玩具", "各類寵物玩具", "個"),
                new CategoryRequest("飼料", "各種寵物飼料", "包"),
                new CategoryRequest("保健品", "寵物專用保健產品", "罐"),
                new CategoryRequest("清潔用品", "清潔與衛生用品", "包"));

        categories.forEach(categoryRequest -> {
            try {
                CategoryResponse response = categoryService.createOrUpdateCategory(categoryRequest);
                if (response.getSuccess()) {
                    System.out.println("成功初始化類別: " + categoryRequest.getCategoryName());
                } else {
                    System.err.println("初始化類別失敗: " + response.getMessage());
                }
            } catch (Exception e) {
                System.err.println("初始化類別失敗: " + e.getMessage());
            }
        });
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
            Admin admin = adminRepository.findByAdminName("admin")
                    .orElseThrow(() -> new IllegalArgumentException("管理員不存在"));

            // 查找類別
            Category category1 = categoryService.findCategoryEntity("飼料");
            Category category2 = categoryService.findCategoryEntity("寵物用品");
            Category category3 = categoryService.findCategoryEntity("玩具");
            Category category4 = categoryService.findCategoryEntity("玩具");
            Category category5 = categoryService.findCategoryEntity("清潔用品");

            Product product1 = new Product();
            product1.setAdmin(admin);
            product1.setProductName("貓糧");
            product1.setDescription("優質貓糧");
            product1.setCategory(category1);
            product1.setUnit(category1.getDefaultUnit());
            product1.setOriginalPrice(BigDecimal.valueOf(20.00));
            product1.setSalePrice(BigDecimal.valueOf(18.00));
            product1.setStockQuantity(100);
            product1.setStatus("上架中");
            product1.setExpire(LocalDate.parse("2025-12-31"));
            product1.setCreatedAt(LocalDateTime.now());
            product1.setUpdatedAt(LocalDateTime.now());
            List<String> filenames1 = List.of("image11.jpg", "image12.jpg");
            productImageService.initializeProductImages(product1, filenames1);

            Product product2 = new Product();
            product2.setAdmin(admin);
            product2.setProductName("狗項圈");
            product2.setDescription("耐用的皮革狗項圈");
            product2.setCategory(category2);
            product2.setUnit(category2.getDefaultUnit());
            product2.setOriginalPrice(BigDecimal.valueOf(15.00));
            product2.setSalePrice(BigDecimal.valueOf(12.50));
            product2.setStockQuantity(200);
            product2.setStatus("上架中");
            product2.setExpire(LocalDate.parse("2030-12-31"));
            product2.setCreatedAt(LocalDateTime.now());
            product2.setUpdatedAt(LocalDateTime.now());
            List<String> filenames2 = List.of("image2.jpg");
            productImageService.initializeProductImages(product2, filenames2);

            Product product3 = new Product();
            product3.setAdmin(admin);
            product3.setProductName("貓抓板");
            product3.setDescription("耐用的貓抓板");
            product3.setCategory(category3);
            product3.setUnit(category3.getDefaultUnit());
            product3.setOriginalPrice(BigDecimal.valueOf(10.00));
            product3.setSalePrice(BigDecimal.valueOf(8.00));
            product3.setStockQuantity(150);
            product3.setStatus("上架中");
            product3.setExpire(LocalDate.parse("2025-12-31"));
            product3.setCreatedAt(LocalDateTime.now());
            product3.setUpdatedAt(LocalDateTime.now());
            List<String> filenames3 = List.of("image3.jpg");
            productImageService.initializeProductImages(product3, filenames3);

            Product product4 = new Product();
            product4.setAdmin(admin);
            product4.setProductName("狗玩具");
            product4.setDescription("耐用的狗玩具");
            product4.setCategory(category4);
            product4.setUnit(category4.getDefaultUnit());
            product4.setOriginalPrice(BigDecimal.valueOf(5.00));
            product4.setSalePrice(BigDecimal.valueOf(4.00));
            product4.setStockQuantity(300);
            product4.setStatus("上架中");
            product4.setExpire(LocalDate.now());
            product4.setCreatedAt(LocalDateTime.now());
            product4.setUpdatedAt(LocalDateTime.now());
            List<String> filenames4 = List.of("image41.jpg", "image42.jpg", "image43.jpg");
            productImageService.initializeProductImages(product4, filenames4);

            Product product5 = new Product();
            product5.setAdmin(admin);
            product5.setProductName("貓砂");
            product5.setDescription("高效吸水貓砂");
            product5.setCategory(category5);
            product5.setUnit(category5.getDefaultUnit());
            product5.setOriginalPrice(BigDecimal.valueOf(25.00));
            product5.setSalePrice(BigDecimal.valueOf(22.00));
            product5.setStockQuantity(80);
            product5.setStatus("上架中");
            product5.setExpire(LocalDate.parse("2025-06-30"));
            product5.setCreatedAt(LocalDateTime.now());
            product5.setUpdatedAt(LocalDateTime.now());
            List<String> filenames5 = List.of("image5.jpg");
            productImageService.initializeProductImages(product5, filenames5);

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
