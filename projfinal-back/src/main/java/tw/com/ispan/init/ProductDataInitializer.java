package tw.com.ispan.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductTag;
// import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.repository.shop.TagRepository;
// import tw.com.ispan.specification.AdminSpecifications;
import tw.com.ispan.specification.CategorySpecifications;
import tw.com.ispan.specification.TagSpecifications;

@Component
@Profile("dev") // 分離測試和開發環境
public class ProductDataInitializer implements CommandLineRunner {

    // @Autowired
    // private AdminRepository adminRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

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
        // 類別名稱與默認單位的映射
        Map<String, String> categories = Map.of(
                "寵物用品", "個",
                "玩具", "個",
                "飼料", "包",
                "保健品", "罐",
                "清潔用品", "套");

        // 遍歷類別
        categories.forEach((categoryName, defaultUnit) -> {
            // 檢查類別是否已存在
            if (!categoryRepository.exists(CategorySpecifications.hasCategoryName(categoryName))) {
                // 創建並保存新的類別
                Category category = new Category();
                category.setCategoryName(categoryName);
                category.setDefaultUnit(defaultUnit); // 設置對應的單位
                categoryRepository.save(category);
                System.out.println("新增類別: " + categoryName + "，單位: " + defaultUnit);
            } else {
                System.out.println("類別已存在: " + categoryName);
            }
        });
    }

    private void initializeTags() {
        List<String> tags = List.of("純天然", "純手作", "低敏", "無添加");
        tags.forEach(tagName -> {
            if (!tagRepository.exists(TagSpecifications.hasTagName(tagName))) {
                ProductTag tag = new ProductTag();
                tag.setTagName(tagName);
                tagRepository.save(tag);
                System.out.println("新增標籤: " + tagName);
            }
        });
    }

    public void initializeData() {

        try {
            // 初始化五組假資料
            Product product1 = new Product();
            product1.setProductName("貓糧");
            product1.setDescription("優質貓糧");
            product1.setOriginalPrice(BigDecimal.valueOf(20.00));
            product1.setSalePrice(BigDecimal.valueOf(18.00));
            product1.setStockQuantity(100);
            product1.setUnit("公斤");
            product1.setStatus("可用");
            product1.setExpire(LocalDate.parse("2025-12-31"));
            product1.setCreatedAt(LocalDateTime.now());
            product1.setUpdatedAt(LocalDateTime.now());

            Category category1 = categoryRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product1.setCategory(category1);
            // product1.setAdmin(new Admin(1, "管理員1"));
            /*
             * 假設 adminId 1 是 "管理員1"；
             * 手動創建的實體處於分離狀態（detached），不能直接關聯到其他實體。
             * 還是需要從adminRepository中查找對應的管理員實體。
             */

            Product product2 = new Product();
            product2.setProductName("狗項圈");
            product2.setDescription("耐用的皮革狗項圈");
            product2.setOriginalPrice(BigDecimal.valueOf(15.00));
            product2.setSalePrice(BigDecimal.valueOf(12.50));
            product2.setStockQuantity(200);
            product2.setUnit("個");
            product2.setStatus("可用");
            product2.setExpire(LocalDate.parse("2030-12-31"));
            product2.setCreatedAt(LocalDateTime.now());
            product2.setUpdatedAt(LocalDateTime.now());
            product2.setCategory(category1);
            // product2.setAdmin(new Admin(2, "管理員2"));

            Product product3 = new Product();
            product3.setProductName("貓抓板");
            product3.setDescription("耐用的貓抓板");
            product3.setOriginalPrice(BigDecimal.valueOf(10.00));
            product3.setSalePrice(BigDecimal.valueOf(8.00));
            product3.setStockQuantity(150);
            product3.setUnit("個");
            product3.setStatus("可用");
            product3.setExpire(LocalDate.parse("2025-12-31"));
            product3.setCreatedAt(LocalDateTime.now());
            product3.setUpdatedAt(LocalDateTime.now());
            product3.setCategory(category1);
            // product3.setAdmin(new Admin(1, "管理員1"));

            Product product4 = new Product();
            product4.setProductName("狗玩具");
            product4.setDescription("耐用的狗玩具");
            product4.setOriginalPrice(BigDecimal.valueOf(5.00));
            product4.setSalePrice(BigDecimal.valueOf(4.00));
            product4.setStockQuantity(300);
            product4.setUnit("個");
            product4.setStatus("可用");
            product4.setExpire(LocalDate.now());
            product4.setCreatedAt(LocalDateTime.now());
            product4.setUpdatedAt(LocalDateTime.now());
            product4.setCategory(category1);
            // product4.setAdmin(new Admin(2, "管理員2"));

            Product product5 = new Product();
            product5.setProductName("貓砂");
            product5.setDescription("高效吸水貓砂");
            product5.setOriginalPrice(BigDecimal.valueOf(25.00));
            product5.setSalePrice(BigDecimal.valueOf(22.00));
            product5.setStockQuantity(80);
            product5.setUnit("公斤");
            product5.setStatus("可用");
            product5.setExpire(LocalDate.parse("2025-06-30"));
            product5.setCreatedAt(LocalDateTime.now());
            product5.setUpdatedAt(LocalDateTime.now());
            product5.setCategory(category1);
            // product5.setAdmin(new Admin(1, "管理員1"));

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);

            System.out.println("初始化假資料成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
