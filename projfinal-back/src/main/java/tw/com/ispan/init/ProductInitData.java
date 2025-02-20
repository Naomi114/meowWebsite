package tw.com.ispan.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductTag;
import tw.com.ispan.dto.shop.CategoryRequest;
import tw.com.ispan.dto.shop.CategoryResponse;
import tw.com.ispan.dto.shop.ProductTagRequest;
import tw.com.ispan.dto.shop.ProductTagResponse;
import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.service.shop.CategoryService;
import tw.com.ispan.service.shop.ProductImageService;
import tw.com.ispan.service.shop.ProductTagService;

@Component
@Order(4)
// @Profile("dev") // 分離測試和開發環境
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
        String adminName = "admin"; // 你的管理員名稱

        // 檢查 `adminName` 是否已存在，避免違反 UNIQUE KEY
        if (adminRepository.findByAdminName(adminName).isPresent()) {
            System.out.println("管理員 " + adminName + " 已存在，跳過初始化");
            return;
        }

        // 🔹 若不存在，則新增
        Admin admin = new Admin();
        admin.setAdminName(adminName);
        admin.setPassword("AAA");
        admin.setCreateDate(LocalDateTime.now());
        admin.setUpdateDate(LocalDateTime.now());

        adminRepository.save(admin);
        System.out.println("初始化管理員成功：" + adminName);
    }

    private void initializeCategories() {
        // LinkedHashSet 會按照插入順序來儲存元素
        Set<CategoryRequest> categories = new LinkedHashSet<>(List.of(
                new CategoryRequest(1, "狗用品", "狗相關商品", "個"),
                new CategoryRequest(2, "貓用品", "貓相關商品", "個"),
                new CategoryRequest(3, "保健品", "寵物專用保健產品", "罐"),
                new CategoryRequest(4, "玩具", "各類寵物玩具", "個"),
                new CategoryRequest(5, "飼料", "各種寵物飼料", "包"),
                new CategoryRequest(6, "清潔用品", "清潔與衛生用品", "包")));

        categories.forEach(categoryRequest -> {
            try {
                // 先檢查 categoryId 是否存在
                boolean exists = categoryService.categoryExistsById(categoryRequest.getCategoryId());
                if (exists) {
                    System.out.println("類別已存在，跳過初始化: " + categoryRequest.getCategoryName());
                } else {
                    CategoryResponse response = categoryService.createOrUpdateCategory(categoryRequest);
                    if (response.getSuccess()) {
                        System.out.println("成功初始化類別: " + categoryRequest.getCategoryName());
                    } else {
                        System.err.println("初始化類別失敗: " + response.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("初始化類別失敗: " + e.getMessage());
            }
        });
    }

    private void initializeTags() {
        List<ProductTagRequest> tags = List.of(
                new ProductTagRequest(1, "純天然", "100% 天然成分，不含人工色素與香料"),
                new ProductTagRequest(2, "純手作", "手工製作，保留食材原始風味"),
                new ProductTagRequest(3, "低敏", "適合敏感體質寵物，減少過敏風險"),
                new ProductTagRequest(4, "無添加", "無化學添加物，守護寵物健康"),
                new ProductTagRequest(5, "高蛋白", "適合活力充沛的毛孩，提供豐富蛋白質"),
                new ProductTagRequest(6, "低脂肪", "健康飲食首選，適合需要控制體重的寵物"),
                new ProductTagRequest(7, "無穀物", "避免穀類過敏，幫助消化與營養吸收"));

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
            Category category1 = categoryService.findCategoryEntity(3);
            Category category2 = categoryService.findCategoryEntity(5);
            Category category3 = categoryService.findCategoryEntity(2);
            Category category4 = categoryService.findCategoryEntity(4);
            Category category5 = categoryService.findCategoryEntity(6);
            Category category6 = categoryService.findCategoryEntity(1);
            Category category7 = categoryService.findCategoryEntity(4);
            Category category8 = categoryService.findCategoryEntity(1);
            Category category9 = categoryService.findCategoryEntity(1);
            Category category10 = categoryService.findCategoryEntity(2);
            Category category11 = categoryService.findCategoryEntity(6);
            Category category12 = categoryService.findCategoryEntity(6);
            Category category13 = categoryService.findCategoryEntity(1);

            List<ProductTag> tag1 = productTagService.findTagEntities(Arrays.asList(3, 4, 5));
            List<ProductTag> tag2 = productTagService.findTagEntities(Collections.singletonList(3));
            List<ProductTag> tag13 = productTagService.findTagEntities(Collections.singletonList(1));

            // 描述欄位換行用，通用不同作業系統: Windows (\r\n), macOS / Linux (\n)
            String newLine = System.lineSeparator();

            // 透過 savedProduct1 先建立實體，存入圖片時才不會因為 Product 處於 transient 無法正確映射

            Product product1 = new Product();
            product1.setAdmin(admin);
            product1.setProductName("貓咪保健品 葉黃素 益生菌 支氣管保健");
            product1.setDescription("Omega-3 & 6，讓毛髮柔順亮麗，提升免疫力。" + newLine +
                    "酥脆口感，適口性極佳，讓貓咪愛不釋口。" + newLine +
                    "現在下單，給主子最安心的營養呵護！");
            product1.setCategory(category1);
            product1.setTags(tag1);
            product1.setUnit(category1.getDefaultUnit());
            product1.setOriginalPrice(BigDecimal.valueOf(490.00));
            product1.setSalePrice(BigDecimal.valueOf(320.00));
            product1.setStockQuantity(218);
            product1.setStatus("上架中");
            product1.setExpire(LocalDate.parse("2028-07-31"));
            product1.setCreatedAt(LocalDateTime.now());
            product1.setUpdatedAt(LocalDateTime.now());
            Product savedProduct1 = productRepository.save(product1);
            List<String> filenames1 = List.of("demo1.jpg", "demo1-1.jpg");
            productImageService.initializeProductImages(savedProduct1, filenames1);

            Product product2 = new Product();
            product2.setAdmin(admin);
            product2.setProductName("美味寵物零食與營養糧食，滿足挑嘴毛孩");
            product2.setDescription("低敏無穀，呵護腸胃，適合敏感體質寵物。" + newLine +
                    "凍乾小食，香脆可口，讓毛孩愛不釋口。" + newLine +
                    "均衡營養配方，守護健康，提升免疫力。" + newLine +
                    "現在下單，給愛寵美味與健康的雙重享受！");
            product2.setCategory(category2);
            product2.setTags(tag2);
            product2.setUnit(category2.getDefaultUnit());
            product2.setOriginalPrice(BigDecimal.valueOf(250.00));
            product2.setSalePrice(BigDecimal.valueOf(180.00));
            product2.setStockQuantity(20);
            product2.setStatus("上架中");
            product2.setExpire(LocalDate.parse("2025-03-31"));
            product2.setCreatedAt(LocalDateTime.now());
            product2.setUpdatedAt(LocalDateTime.now());
            Product savedProduct2 = productRepository.save(product2);
            List<String> filenames2 = List.of("image2.jpg");
            productImageService.initializeProductImages(savedProduct2, filenames2);

            Product product3 = new Product();
            product3.setAdmin(admin);
            product3.setProductName("貓抓板");
            product3.setDescription("釋放壓力，滿足磨爪需求，讓貓咪更健康快樂。" + newLine +
                    "多種尺寸與造型，適合各種貓咪習慣。" + newLine +
                    "現在下單，給主子一個專屬的磨爪樂園！");
            product3.setCategory(category3);
            product3.setUnit(category3.getDefaultUnit());
            product3.setOriginalPrice(BigDecimal.valueOf(10.00));
            product3.setSalePrice(BigDecimal.valueOf(8.00));
            product3.setStockQuantity(150);
            product3.setStatus("上架中");
            product3.setExpire(LocalDate.parse("2025-12-31"));
            product3.setCreatedAt(LocalDateTime.now());
            product3.setUpdatedAt(LocalDateTime.now());
            Product savedProduct3 = productRepository.save(product3);
            List<String> filenames3 = List.of("demo31.jpg", "demo32.jpg", "demo33.jpg");
            productImageService.initializeProductImages(savedProduct3, filenames3);

            Product product4 = new Product();
            product4.setAdmin(admin);
            product4.setProductName("狗玩具");
            product4.setDescription("內建發聲裝置，吸引注意力，防止破壞家具。" + newLine +
                    "適合大小型犬，訓練與娛樂兼具，讓狗狗更快樂。" + newLine +
                    "現在下單，陪伴毛孩每一刻的歡樂時光！");
            product4.setCategory(category4);
            product4.setUnit(category4.getDefaultUnit());
            product4.setOriginalPrice(BigDecimal.valueOf(100.00));
            product4.setSalePrice(BigDecimal.valueOf(59.00));
            product4.setStockQuantity(300);
            product4.setStatus("上架中");
            product4.setExpire(LocalDate.now());
            product4.setCreatedAt(LocalDateTime.now());
            product4.setUpdatedAt(LocalDateTime.now());
            Product savedProduct4 = productRepository.save(product4);
            List<String> filenames4 = List.of("demo41.jpg", "demo42.jpg");
            productImageService.initializeProductImages(savedProduct4, filenames4);

            Product product5 = new Product();
            product5.setAdmin(admin);
            product5.setProductName("混合貓砂 原味 咖啡 白玉豆腐 經典版 豆腐砂");
            product5.setDescription("瞬間凝結，方便鏟除，讓清理更輕鬆。" + newLine +
                    "多種材質選擇，滿足不同貓咪需求。" + newLine +
                    "現在下單，打造乾淨無味的舒適貓廁所！");
            product5.setCategory(category5);
            product5.setUnit(category5.getDefaultUnit());
            product5.setOriginalPrice(BigDecimal.valueOf(145.00));
            product5.setSalePrice(BigDecimal.valueOf(140.00));
            product5.setStockQuantity(20000);
            product5.setStatus("上架中");
            product5.setExpire(LocalDate.parse("2029-05-30"));
            product5.setCreatedAt(LocalDateTime.now());
            product5.setUpdatedAt(LocalDateTime.now());
            Product savedProduct5 = productRepository.save(product5);
            List<String> filenames5 = List.of("demo51.jpg");
            productImageService.initializeProductImages(savedProduct5, filenames5);

            Product product6 = new Product();
            product6.setAdmin(admin);
            product6.setProductName("【蜂巢透氣 不勒脖】狗狗胸背帶 胸背帶 寵物背帶 貓胸背帶 中型犬 大型犬 夜間反光 狗胸背帶 寵物背心 防爆衝胸背帶");
            product6.setDescription("帶上舒適，走遍世界" + newLine +
                    "狗狗出門散步，安全與舒適最重要！" + newLine +
                    "超服貼透氣｜一整天不悶熱" + newLine +
                    "可調節設計｜小型犬到大型犬都適用" + newLine +
                    "可自由調整鬆緊度，讓每隻狗狗都能擁有最剛好的貼合度！" + newLine +
                    "讓狗狗散步更舒適，安心陪伴每一步！");
            product6.setCategory(category6);
            product6.setUnit(category6.getDefaultUnit());
            product6.setOriginalPrice(BigDecimal.valueOf(200.00));
            product6.setSalePrice(BigDecimal.valueOf(159.00));
            product6.setStockQuantity(50);
            product6.setStatus("上架中");
            product6.setExpire(LocalDate.parse("2030-12-30"));
            product6.setCreatedAt(LocalDateTime.now());
            product6.setUpdatedAt(LocalDateTime.now());
            Product savedProduct6 = productRepository.save(product6);
            List<String> filenames6 = List.of("demo6.jpg");
            productImageService.initializeProductImages(savedProduct6, filenames6);

            Product product7 = new Product();
            product7.setAdmin(admin);
            product7.setProductName("精品優選 寵物小墨鏡 寵物眼鏡 寵物變裝 貓墨鏡 貓眼鏡 狗狗眼鏡 狗狗墨鏡 寵物太陽眼鏡 拍照道具");
            product7.setDescription("潮流新風尚，毛孩專屬時尚眼鏡" + newLine +
                    "萌寵有型，吸睛百分百！" + newLine +
                    "帶上這副寵物專屬眼鏡，瞬間變身潮流寵物，走到哪都是焦點！" + newLine +
                    "舒適輕盈，貼合不壓迫" + newLine +
                    "高彈力鏡架，柔軟鼻墊設計，讓毛孩自在佩戴不抗拒！" + newLine +
                    "時尚百搭，多色選擇");
            product7.setCategory(category7);
            product7.setUnit(category7.getDefaultUnit());
            product7.setOriginalPrice(BigDecimal.valueOf(50.00));
            product7.setSalePrice(BigDecimal.valueOf(35.00));
            product7.setStockQuantity(80);
            product7.setStatus("上架中");
            product7.setExpire(LocalDate.parse("2026-06-30"));
            product7.setCreatedAt(LocalDateTime.now());
            product7.setUpdatedAt(LocalDateTime.now());
            Product savedProduct7 = productRepository.save(product7);
            List<String> filenames7 = List.of("demo7.jpg");
            productImageService.initializeProductImages(savedProduct7, filenames7);

            Product product8 = new Product();
            product8.setAdmin(admin);
            product8.setProductName("大型犬胸背帶 穩固牢靠");
            product8.setDescription("強壯大狗，安心掌控！大型犬專用胸背帶" + newLine +
                    "大狗力量大？不用怕，這款胸背帶讓你輕鬆駕馭！" + newLine +
                    "採用軍規級尼龍材質，超強耐拉扯，適合哈士奇、德牧、拉布拉多、秋田犬等大型犬！" + newLine +
                    "分散受力｜減少脖子壓迫！");
            product8.setCategory(category8);
            product8.setUnit(category8.getDefaultUnit());
            product8.setOriginalPrice(BigDecimal.valueOf(280.00));
            product8.setSalePrice(BigDecimal.valueOf(199.00));
            product8.setStockQuantity(150);
            product8.setStatus("上架中");
            product8.setExpire(LocalDate.parse("2028-06-30"));
            product8.setCreatedAt(LocalDateTime.now());
            product8.setUpdatedAt(LocalDateTime.now());
            Product savedProduct8 = productRepository.save(product8);
            List<String> filenames8 = List.of("demo8.jpg");
            productImageService.initializeProductImages(savedProduct8, filenames8);

            Product product9 = new Product();
            product9.setAdmin(admin);
            product9.setProductName(" 貓用狗用防嗆奶瓶 幼貓新生寵物幼犬瓶小狗狗餵奶器 寵物餵食奶嘴 耐咬小貓新生幼崽犬餵奶神器奶嘴");
            product9.setDescription("專為新生貓狗設計，呵護每一口營養" + newLine +
                    "適用於幼貓、幼犬、新生寵物，模擬母乳餵養，讓寶貝安心喝奶。" + newLine +
                    "採用食品級矽膠材質，柔軟不傷口腔，耐咬耐用，安全放心。" + newLine +
                    "現在下單，給你的新生寶貝最貼心的呵護！");
            product9.setCategory(category9);
            product9.setUnit(category9.getDefaultUnit());
            product9.setOriginalPrice(BigDecimal.valueOf(100.00));
            product9.setSalePrice(BigDecimal.valueOf(92.00));
            product9.setStockQuantity(999);
            product9.setStatus("上架中");
            product9.setExpire(LocalDate.parse("2026-10-30"));
            product9.setCreatedAt(LocalDateTime.now());
            product9.setUpdatedAt(LocalDateTime.now());
            Product savedProduct9 = productRepository.save(product9);
            List<String> filenames9 = List.of("demo9.jpg", "demo91.jpg");
            productImageService.initializeProductImages(savedProduct9, filenames9);

            Product product10 = new Product();
            product10.setAdmin(admin);
            product10.setProductName("寵物窩 貓窩 貓床墊 寵物床墊 半開放式貓窩 小型犬窩 寵物睡床 寵物睡墊");
            product10.setDescription("半開放式設計，安全感滿分，冬天保暖，夏天透氣。" + newLine +
                    "厚實床墊，柔軟支撐，呵護關節健康，讓毛孩一夜好眠。" + newLine +
                    "可拆洗設計，清潔方便，保持乾淨衛生。" + newLine +
                    "現在下單，給愛寵一個舒適的專屬窩！");
            product10.setCategory(category10);
            product10.setUnit(category10.getDefaultUnit());
            product10.setOriginalPrice(BigDecimal.valueOf(368.00));
            product10.setSalePrice(BigDecimal.valueOf(258.00));
            product10.setStockQuantity(200);
            product10.setStatus("上架中");
            product10.setExpire(LocalDate.parse("2999-06-30"));
            product10.setCreatedAt(LocalDateTime.now());
            product10.setUpdatedAt(LocalDateTime.now());
            Product savedProduct10 = productRepository.save(product10);
            List<String> filenames10 = List.of("demo10.jpg");
            productImageService.initializeProductImages(savedProduct10, filenames10);

            Product product11 = new Product();
            product11.setAdmin(admin);
            product11.setProductName("寵物剪刀");
            product11.setDescription("圓角安全設計，防止誤傷，適合新手與專業美容師。" + newLine +
                    "適用貓犬各類毛髮，讓毛孩時刻保持清爽整潔。" + newLine +
                    "現在下單，讓愛寵擁有專業級修剪體驗！");
            product11.setCategory(category11);
            product11.setUnit(category11.getDefaultUnit());
            product11.setOriginalPrice(BigDecimal.valueOf(299.00));
            product11.setSalePrice(BigDecimal.valueOf(250.00));
            product11.setStockQuantity(10);
            product11.setStatus("上架中");
            product11.setExpire(LocalDate.parse("2999-12-30"));
            product11.setCreatedAt(LocalDateTime.now());
            product11.setUpdatedAt(LocalDateTime.now());
            Product savedProduct11 = productRepository.save(product11);
            List<String> filenames11 = List.of("demo11.jpg");
            productImageService.initializeProductImages(savedProduct11, filenames11);

            Product product12 = new Product();
            product12.setAdmin(admin);
            product12.setProductName("寵物清潔用品");
            product12.setDescription("除臭抗菌，清新環境，讓居家空氣更潔淨。" + newLine +
                    "天然配方，呵護肌膚，減少敏感與不適。" + newLine +
                    "居家與外出皆適用，讓毛孩時刻保持清爽潔淨。" + newLine +
                    "現在下單，讓愛寵享受更安心的清潔呵護！");
            product12.setCategory(category12);
            product12.setUnit(category12.getDefaultUnit());
            product12.setOriginalPrice(BigDecimal.valueOf(212.00));
            product12.setSalePrice(BigDecimal.valueOf(22.00));
            product12.setStockQuantity(120);
            product12.setStatus("上架中");
            product12.setExpire(LocalDate.parse("2025-11-04"));
            product12.setCreatedAt(LocalDateTime.now());
            product12.setUpdatedAt(LocalDateTime.now());
            Product savedProduct12 = productRepository.save(product12);
            List<String> filenames12 = List.of("demo12.jpg");
            productImageService.initializeProductImages(savedProduct12, filenames12);

            Product product13 = new Product();
            product13.setAdmin(admin);
            product13.setProductName("寵物狗碗 慢餵食器 益智狗食碗 防吞食盤");
            product13.setDescription("慢食碗設計，防止狼吞虎嚥，促進消化健康。" + newLine +
                    "多種尺寸選擇，適合小型到大型犬使用。" + newLine +
                    "現在下單，給毛孩更舒適的用餐體驗！");
            product13.setCategory(category13);
            product13.setTags(tag13);
            product13.setUnit(category13.getDefaultUnit());
            product13.setOriginalPrice(BigDecimal.valueOf(65.00));
            product13.setSalePrice(BigDecimal.valueOf(55.00));
            product13.setStockQuantity(53);
            product13.setStatus("上架中");
            product13.setExpire(LocalDate.parse("2030-08-08"));
            product13.setCreatedAt(LocalDateTime.now());
            product13.setUpdatedAt(LocalDateTime.now());
            Product savedProduct13 = productRepository.save(product13);
            List<String> filenames13 = List.of("demo13.jpg", "demo13-1.jpg", "demo13-2.jpg", "demo13-3.jpg",
                    "demo13-4.jpg");
            productImageService.initializeProductImages(savedProduct13, filenames13);

            productRepository.save(savedProduct1);
            productRepository.save(savedProduct2);
            productRepository.save(savedProduct3);
            productRepository.save(savedProduct4);
            productRepository.save(savedProduct5);
            productRepository.save(savedProduct6);
            productRepository.save(savedProduct7);
            productRepository.save(savedProduct8);
            productRepository.save(savedProduct9);
            productRepository.save(savedProduct10);
            productRepository.save(savedProduct11);
            productRepository.save(savedProduct12);
            productRepository.save(savedProduct13);

            System.out.println("初始化商城資料成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
