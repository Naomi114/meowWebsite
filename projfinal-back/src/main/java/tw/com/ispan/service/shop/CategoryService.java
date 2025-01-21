package tw.com.ispan.service.shop;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.repository.shop.CategoryRepository;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 根據類別名稱抓取或創建類別
     *
     * @param categoryName 類別名稱
     * @param unit         預設單位（用於創建新類別）
     * @return Category
     */
    public Category findOrCreateCategory(String categoryName, String unit) {
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);

        if (categoryOpt.isPresent()) {
            Category existingCategory = categoryOpt.get();
            if (existingCategory.getDefaultUnit() == null || existingCategory.getDefaultUnit().isEmpty()) {
                existingCategory.setDefaultUnit(unit);
                categoryRepository.save(existingCategory);
                System.out.println("更新類別: " + categoryName + "，預設單位設置為: " + unit);
            } else {
                System.out.println("類別已存在: " + categoryName + "，預設單位: " + existingCategory.getDefaultUnit());
            }
            return existingCategory;
        } else {
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryName);
            newCategory.setDefaultUnit(unit); // 使用傳入的單位
            categoryRepository.save(newCategory);
            System.out.println("新增類別: " + categoryName + "，單位: " + unit);
            return newCategory;
        }
    }

}
