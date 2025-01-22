package tw.com.ispan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.dto.CategoryRequest;
import tw.com.ispan.dto.CategoryResponse;
import tw.com.ispan.service.shop.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 新增或更新類別
    @PostMapping
    public ResponseEntity<CategoryResponse> createOrUpdateCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.createOrUpdateCategory(request);
        return ResponseEntity.ok(response);
    }

    // 修改類別描述
    @PutMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> updateCategoryDescription(
            @PathVariable String categoryName,
            @RequestBody @Valid CategoryRequest request) {
        if (!categoryName.equals(request.getCategoryName())) {
            throw new IllegalArgumentException("URL 中的類別名稱與請求體中的類別名稱不一致");
        }
        CategoryResponse response = categoryService.updateCategoryDescription(categoryName,
                request.getCategoryDescription());
        return ResponseEntity.ok(response);
    }

    // 模糊查詢
    @GetMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> searchCategory(@PathVariable String categoryName) {
        CategoryResponse response = categoryService.findCategory(categoryName);
        return ResponseEntity.ok(response);
    }

    // 刪除類別
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable String categoryName) {
        CategoryResponse response = categoryService.deleteCategory(categoryName);
        return ResponseEntity.ok(response);
    }
}
