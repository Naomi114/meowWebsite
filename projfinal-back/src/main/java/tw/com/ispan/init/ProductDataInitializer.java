package tw.com.ispan.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.shop.CategoryBean;
import tw.com.ispan.domain.shop.ProductBean;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.repository.shop.ProductRepository;

@Component
public class ProductDataInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void initializeData() {

        try {
            // 初始化五組假資料
            ProductBean product1 = new ProductBean();
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
            CategoryBean category1 = categoryRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product1.setCategory(category1); // 假設 categoryId 1 是 "寵物用品"
            product1.setAdmin(new Admin(1,"管理員1")); // 假設 adminId 1 是 "管理員1"

            ProductBean product2 = new ProductBean();
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
            product2.setAdmin(new Admin(2, "管理員2"));

            ProductBean product3 = new ProductBean();
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
            product3.setAdmin(new Admin(1, "管理員1"));

            ProductBean product4 = new ProductBean();
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
            product4.setAdmin(new Admin(2, "管理員2"));

            ProductBean product5 = new ProductBean();
            product5.setProductName("貓砂");
            product5.setDescription("高效吸水貓砂");
            product5.setOriginalPrice(BigDecimal.valueOf(25.00));
            product5.setSalePrice(BigDecimal.valueOf(22.00));
            product5.setStockQuantity(80);
            product5.setUnit("公斤");
            product5.setStatus("可用");
            product5.setExpire(LocalDate.parse("2025-06-31"));
            product5.setCreatedAt(LocalDateTime.now());
            product5.setUpdatedAt(LocalDateTime.now());
            product5.setCategory(category1);
            product5.setAdmin(new Admin(1, "管理員1"));

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
