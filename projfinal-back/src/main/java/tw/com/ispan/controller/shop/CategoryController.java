package tw.com.ispan.controller.shop;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.dto.shop.CategoryRequest;
import tw.com.ispan.dto.shop.CategoryResponse;
import tw.com.ispan.service.shop.CategoryService;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 新增或更新 (新增商品時，選用預設單位或新建會用到)
    @PostMapping
    public ResponseEntity<CategoryResponse> createOrUpdateCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.createOrUpdateCategory(request);
        return ResponseEntity.ok(response);
    }

    // 單筆修改
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer id,
            @RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.updateSingle(id, request);
        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 刪除類別
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Integer id) {
        CategoryResponse response = categoryService.deleteSingle(id);
        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 單筆查詢，返回商品清單
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findCategoryWithProducts(@PathVariable Integer id) {
        CategoryResponse response = categoryService.getProductsByCategory(id);
        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 模糊搜尋類別及其商品
    @GetMapping("/search")
    public ResponseEntity<CategoryResponse> findCategoriesWithProducts(@RequestParam String keyword) {
        CategoryResponse response = categoryService.findCategoriesWithProducts(keyword);
        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 獲取所有分類
    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse response = categoryService.getAllCategories();
        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
