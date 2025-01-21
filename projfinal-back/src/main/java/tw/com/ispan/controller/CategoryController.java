package tw.com.ispan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.dto.CategoryRequest;
import tw.com.ispan.dto.CategoryResponse;
import tw.com.ispan.service.shop.CategoryService;

/* 
    查詢現有類別
    根據名稱創建新類別
 */

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 新增類別
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        Category category = categoryService.saveOrUpdateCategory(
                request.getCategoryName(),
                request.getCategoryDescription(),
                request.getUnit()
        );
        CategoryResponse response = new CategoryResponse();
        response.setCategoryName(category.getCategoryName());
        response.setDefaultUnit(category.getDefaultUnit());
        response.setCategoryDescription(category.getCategoryDescription());
        return ResponseEntity.ok(response);
    }

    // 修改類別描述
    @PutMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String categoryName,
            @RequestBody @Valid CategoryRequest request) {
        if (!categoryName.equals(request.getCategoryName())) {
            throw new IllegalArgumentException("URL 中的類別名稱與請求體中的類別名稱不一致");
        }

        Category category = categoryService.updateCategoryDescription(
                categoryName,
                request.getCategoryDescription()
        );
        CategoryResponse response = new CategoryResponse();
        response.setCategoryName(category.getCategoryName());
        response.setCategoryDescription(category.getCategoryDescription());
        return ResponseEntity.ok(response);
    }

}
