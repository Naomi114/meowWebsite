package tw.com.ispan.service.shop;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.shop.CategoryDTO;
import tw.com.ispan.dto.shop.CategoryRequest;
import tw.com.ispan.dto.shop.CategoryResponse;
import tw.com.ispan.dto.shop.ProductDTO;
import tw.com.ispan.dto.shop.ProductRequest;
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

                if (request.getCategoryId() != null) {
                    category.setCategoryId(request.getCategoryId());
                }

                if (request.getDefaultUnit() != null && !request.getDefaultUnit().isEmpty()) {
                    category.setDefaultUnit(request.getDefaultUnit());
                } else if (category.getDefaultUnit() == null || category.getDefaultUnit().isEmpty()) {
                    throw new IllegalArgumentException("類別的預設單位未設置");
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
                categoryRepository.saveAndFlush(category); // ✅ 確保立即儲存，避免 ID 遺失
            }
            Category updatedCategory = categoryRepository.save(category);

            CategoryDTO categoryDTO = new CategoryDTO(updatedCategory); // 將 category 轉換為 CategoryDTO
            response.setCategories(Collections.singletonList(categoryDTO));
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

                // ✅ 修正這裡，將 category 轉換為 CategoryDTO
                CategoryDTO categoryDTO = new CategoryDTO(updatedCategory);
                response.setCategories(Collections.singletonList(categoryDTO));
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
    public CategoryResponse getProductsByCategory(Integer categoryId) {
        CategoryResponse response = new CategoryResponse();
        try {
            // 取得類別
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("類別不存在: ID = " + categoryId));

            // 取得該類別下的所有商品
            List<Product> products = productRepository.findByCategory(category);

            // 確保包含 images[]，轉換 Product 為 ProductDTO
            // List<ProductDTO> productDTOs = products.stream()
            // .map(ProductDTO::new) // 使用 ProductDTO 構造函數轉換，確保 images[] 存在
            // .collect(Collectors.toList());

            // 轉換為 CategoryDTO
            CategoryDTO categoryDTO = new CategoryDTO(category);

            // 設置 Response
            response.setCategories(Collections.singletonList(categoryDTO));
            response.setSuccess(true);
            response.setMessage("查詢成功");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("查詢失敗: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    // 模糊搜尋類別及其商品
    public CategoryResponse findCategoriesWithProducts(String keyword) {
        CategoryResponse response = new CategoryResponse();
        try {
            // 1️⃣ 查詢名稱符合關鍵字的類別
            List<Category> categories = categoryRepository.findByCategoryNameContaining(keyword);

            if (categories.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("沒有匹配的類別");
                return response;
            }

            // 2️⃣ 將 `Category` 轉為 `CategoryDTO`
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(category -> {
                        // 🔹 查詢該類別的商品並轉換為 `ProductDTO`
                        // List<ProductDTO> productDTOs = category.getProducts().stream()
                        // .map(ProductDTO::new) // 確保 `ProductDTO` 正確轉換
                        // .collect(Collectors.toList());

                        return new CategoryDTO(category);
                    })
                    .collect(Collectors.toList());

            // 3️⃣ 設置回應
            response.setSuccess(true);
            response.setMessage("模糊查詢成功");
            response.setCategories(categoryDTOs);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("模糊查詢失敗: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    // 查詢所有類別
    public CategoryResponse getAllCategories() {
        CategoryResponse response = new CategoryResponse();
        try {
            // 1️⃣ 查詢所有類別
            List<Category> categories = categoryRepository.findAll();

            if (categories.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("沒有可用的類別");
                return response;
            }

            // 2️⃣ 轉換 `Category` → `CategoryDTO`
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(category -> {
                        // 🔹 查詢該類別的商品並轉換為 `ProductDTO`
                        // List<ProductDTO> productDTOs = category.getProducts().stream()
                        // .map(ProductDTO::new) // 確保 `ProductDTO` 正確轉換
                        // .collect(Collectors.toList());

                        return new CategoryDTO(category);
                    })
                    .collect(Collectors.toList());

            // 3️⃣ 設置回應
            response.setSuccess(true);
            response.setMessage("成功獲取類別清單");
            response.setCategories(categoryDTOs);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("獲取類別失敗: " + e.getMessage());
            e.printStackTrace();
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
            Integer categoryId = categoryResponse.getCategories().get(0).getCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("類別不存在: ID = " + categoryId));
            product.setCategory(category);

            // 設定商品單位
            String unit = categoryRequest.getDefaultUnit(); // **先使用前端傳入的 defaultUnit**
            if (unit == null || unit.isEmpty()) {
                unit = category.getDefaultUnit(); // **若前端沒傳入，則使用資料庫內的 defaultUnit**
            }
            if (unit == null || unit.isEmpty()) {
                throw new IllegalArgumentException("類別的預設單位未設置或提供");
            }
            product.setUnit(unit);
        }
    }

    // 初始化查詢實體 CategoryResponse => Category
    public Category findCategoryEntity(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("類別不存在: " + categoryId));
    }

    // 型別轉換: ProductRequest => CategoryRequest
    public CategoryRequest buildCategoryRequestFromProduct(ProductRequest request) {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName(request.getCategoryName());
        categoryRequest.setCategoryDescription(request.getCategoryDescription());
        categoryRequest.setDefaultUnit(request.getUnit());
        return categoryRequest;
    }

    // 已上架商品，修改類別
    public Integer findCategoryIdByName(String categoryName) {
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
        if (categoryOpt.isPresent()) {
            return categoryOpt.get().getCategoryId();
        } else {
            throw new IllegalArgumentException("類別不存在: " + categoryName);
        }
    }

    public boolean categoryExistsById(Integer categoryId) {
        try {
            return categoryRepository.existsById(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
