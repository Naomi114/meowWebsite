package tw.com.ispan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.service.shop.CategoryService;

/*
    確保與實際資料庫交互，能正確保存和更新 Category 的資料。
    驗證初始化資料是否影響 findOrCreateCategory 的行為。
*/

@SpringBootTest
@Transactional
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        categoryRepository.save(new Category("寵物用品", "個"));
        categoryRepository.save(new Category("飼料", "包"));
        categoryRepository.save(new Category("玩具", "件"));
    }

    @Test
    void testFindOrCreateCategoryIntegration() {
        // 初始化資料
        categoryRepository.save(new Category("項圈", "個"));

        // 測試已存在類別
        Category existingCategory = categoryService.findOrCreateCategory("玩具", "件");
        assertEquals("件", existingCategory.getDefaultUnit());

        // 測試新建類別
        Category newCategory = categoryService.findOrCreateCategory("清潔用品", "袋");
        assertEquals("袋", newCategory.getDefaultUnit());
    }
}
