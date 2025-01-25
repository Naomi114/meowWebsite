package tw.com.ispan.service.shop;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                // 更新類別
                category = existingCategory.get();
                if (request.getDefaultUnit() != null && !request.getDefaultUnit().isEmpty()) {
                    category.setDefaultUnit(request.getDefaultUnit());
                }
                if (request.getCategoryDescription() != null) {
                    category.setCategoryDescription(request.getCategoryDescription());
                }
            } else {
                // 新增類別
                category = new Category();
                category.setCategoryName(request.getCategoryName());
                category.setCategoryDescription(request.getCategoryDescription());
                category.setDefaultUnit(request.getDefaultUnit());

                // 驗證必要屬性是否設置
                if (category.getCategoryName() == null || category.getDefaultUnit() == null) {
                    throw new IllegalArgumentException("類別名稱或預設單位不能為空");
                }

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
                category.setDefaultUnit(request.getDefaultUnit());
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

    // 商品上架，處理類別
	public void processCategory(Product product, Set<CategoryRequest> categoryRequests) {
        if (categoryRequests == null || categoryRequests.isEmpty()) {
            throw new IllegalArgumentException("必須提供至少一個類別");
        }
    
        for (CategoryRequest categoryRequest : categoryRequests) {
            // 查詢或創建類別
            CategoryResponse categoryResponse = createOrUpdateCategory(categoryRequest);
            if (!categoryResponse.getSuccess()) {
                throw new IllegalArgumentException("類別操作失敗: " + categoryResponse.getMessage());
            }
    
            // 查詢完整的 Category 實體
            Category category = findCategoryEntity(categoryResponse.getCategoryName());
            product.setCategory(category);
    
            // 設定商品單位
            String unit = categoryRequest.getDefaultUnit();
            if (unit == null || unit.isEmpty()) {
                unit = categoryResponse.getDefaultUnit();
                if (unit == null || unit.isEmpty()) {
                    throw new IllegalArgumentException("類別的預設單位未設置或提供");
                }
            }
            product.setUnit(unit);
        }
    }
    
    // 查詢實體 CategoryResponse => Category
    public Category findCategoryEntity(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("類別不存在: " + categoryName));
    }

    // 型別轉換: ProductRequest => CategoryRequest
    public CategoryRequest buildCategoryRequestFromProduct(ProductRequest request) {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName(request.getCategoryName());
        categoryRequest.setCategoryDescription(request.getCategoryDescription());
        categoryRequest.setDefaultUnit(request.getUnit());
        return categoryRequest;
    }
}
