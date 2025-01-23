package tw.com.ispan.service.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.CategoryRequest;
import tw.com.ispan.dto.CategoryResponse;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.repository.shop.ProductRepository;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    // 單筆新增或更新
    public CategoryResponse createOrUpdateCategory(CategoryRequest request) {
        CategoryResponse response = new CategoryResponse();
        try {
            Optional<Category> existingCategory = categoryRepository.findByCategoryName(request.getCategoryName());
            Category category;
            if (existingCategory.isPresent()) {
                category = existingCategory.get();
                if (request.getUnit() != null && !request.getUnit().isEmpty()) {
                    category.setDefaultUnit(request.getUnit());
                }
            } else {
                if (request.getUnit() == null || request.getUnit().isEmpty()) {
                    throw new IllegalArgumentException("新增類別時必須提供預設單位");
                }
                category = new Category();
                category.setCategoryName(request.getCategoryName());
                category.setCategoryDescription(request.getCategoryDescription());
                category.setDefaultUnit(request.getUnit());
                categoryRepository.save(category);
            }
            response.setCategoryName(category.getCategoryName());
            response.setCategoryDescription(category.getCategoryDescription());
            response.setDefaultUnit(category.getDefaultUnit());
            response.setSuccess(true);
            response.setMessage("類別操作成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("類別操作失敗: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    // 單筆刪除
    public CategoryResponse deleteSingle(Integer categoryId) {
        CategoryResponse response = new CategoryResponse();
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                categoryRepository.delete(categoryOpt.get());
                response.setSuccess(true);
                response.setMessage("類別刪除成功");
            } else {
                response.setSuccess(false);
                response.setMessage("類別不存在");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("類別刪除失敗: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    // 單筆修改
    public CategoryResponse updateSingle(Integer categoryId, CategoryRequest request) {
        CategoryResponse response = new CategoryResponse();
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                category.setCategoryName(request.getCategoryName());
                category.setCategoryDescription(request.getCategoryDescription());
                category.setDefaultUnit(request.getUnit());
                Category updatedCategory = categoryRepository.save(category);

                // 設置返回
                response.setCategoryName(updatedCategory.getCategoryName());
                response.setCategoryDescription(updatedCategory.getCategoryDescription());
                response.setDefaultUnit(updatedCategory.getDefaultUnit());
                response.setSuccess(true);
                response.setMessage("類別描述更新成功");
            } else {
                response.setSuccess(false);
                response.setMessage("類別不存在");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("類別描述更新失敗: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    // 單一類別查詢，返回商品清單
    public CategoryResponse findCategoryWithProducts(Integer categoryId) {
        CategoryResponse response = new CategoryResponse();
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("類別不存在: ID = " + categoryId));

            List<Product> products = productRepository.findByCategory(category);

            response.setCategoryName(category.getCategoryName());
            response.setProducts(products);
            response.setSuccess(true);
            response.setMessage("類別查詢成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("類別查詢失敗: " + e.getMessage());
        }
        return response;
    }

    // 模糊搜尋類別及其商品
    public CategoryResponse findCategoriesWithProducts(String keyword) {
        CategoryResponse response = new CategoryResponse();
        try {
            List<Category> categories = categoryRepository.findByCategoryNameContaining(keyword);

            if (categories.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("沒有匹配的類別");
                return response;
            }

            List<Product> products = productRepository.findByCategoryIn(categories);

            response.setCategories(categories);
            response.setProducts(products);
            response.setSuccess(true);
            response.setMessage("模糊查詢成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("模糊查詢失敗: " + e.getMessage());
        }
        return response;
    }

    // 轉換 CategoryResponse => Category
    public Category findCategoryEntity(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("類別不存在: " + categoryName));
    }

    // 單選類別型別轉換: ProductRequest => CategoryRequest
    public CategoryRequest buildCategoryRequestFromProduct(ProductRequest request) {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName(request.getCategoryName());
        categoryRequest.setCategoryDescription(request.getCategoryDescription());
        categoryRequest.setUnit(request.getUnit());
        return categoryRequest;
    }
}
