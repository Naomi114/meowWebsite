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

    public Category findCategory(String categoryName, String unit) {
        return categoryRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new IllegalArgumentException("類別不存在: " + categoryName));
    }

    public Category updateCategoryDescription(String categoryName, String categoryDescription) {
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
    
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setCategoryDescription(categoryDescription);
            return categoryRepository.save(category);
        } else {
            throw new IllegalArgumentException("類別不存在: " + categoryName);
        }
    }
    
    public Category saveOrUpdateCategory(String categoryName, String categoryDescription, String unit) {
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
    
        if (categoryOpt.isPresent()) {
            Category existingCategory = categoryOpt.get();
            if (existingCategory.getDefaultUnit() == null || existingCategory.getDefaultUnit().isEmpty()) {
                existingCategory.setDefaultUnit(unit);
            }
            if (existingCategory.getCategoryDescription() == null || existingCategory.getCategoryDescription().isEmpty()) {
                existingCategory.setCategoryDescription(categoryDescription);
            }
            categoryRepository.save(existingCategory);
            System.out.println("更新類別: " + categoryName);
            return existingCategory;
        } else {
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryName);
            newCategory.setDefaultUnit(unit);
            newCategory.setCategoryDescription(categoryDescription);
            categoryRepository.save(newCategory);
            System.out.println("新增類別: " + categoryName);
            return newCategory;
        }
    }
    
}
