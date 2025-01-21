package tw.com.ispan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.service.shop.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    // 當類別存在且 defaultUnit 已設置時，應返回正確的 defaultUnit。
    @Test
    void testFindOrCreateCategoryWhenCategoryExistsWithDefaultUnit() {
        // Arrange
        Category existingCategory = new Category();
        existingCategory.setCategoryName("寵物用品");
        existingCategory.setDefaultUnit("個");

        when(categoryRepository.findByCategoryName("寵物用品"))
                .thenReturn(Optional.of(existingCategory));

        // Act
        Category result = categoryService.findOrCreateCategory("寵物用品", "包");

        // Assert
        assertEquals("個", result.getDefaultUnit());
        verify(categoryRepository, never()).save(any());
    }

    // 當類別存在但 defaultUnit 為空時，應更新並返回設置後的 defaultUnit。
    @Test
    void testFindOrCreateCategoryWhenCategoryExistsWithoutDefaultUnit() {
        // Arrange
        Category existingCategory = new Category();
        existingCategory.setCategoryName("飼料");
        existingCategory.setDefaultUnit(null);

        when(categoryRepository.findByCategoryName("飼料"))
                .thenReturn(Optional.of(existingCategory));

        // Act
        Category result = categoryService.findOrCreateCategory("飼料", "包");

        // Assert
        assertEquals("包", result.getDefaultUnit());
        verify(categoryRepository).save(existingCategory);
    }

    // 當類別不存在時，應創建新類別並設置 defaultUnit。
    @Test
    void testFindOrCreateCategoryWhenCategoryDoesNotExist() {
        // Arrange
        when(categoryRepository.findByCategoryName("清潔用品"))
                .thenReturn(Optional.empty());

        // Act
        Category result = categoryService.findOrCreateCategory("清潔用品", "袋");

        // Assert
        assertEquals("袋", result.getDefaultUnit());
        verify(categoryRepository).save(any());
    }

}
