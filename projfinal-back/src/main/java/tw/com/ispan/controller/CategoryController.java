package tw.com.ispan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/findOrCreate")
    public ResponseEntity<CategoryResponse> findOrCreateCategory(@RequestBody CategoryRequest request) {
        Category category = categoryService.findOrCreateCategory(request.getCategoryName(), request.getUnit());
        System.out.println("unit=" + request.getUnit());
        CategoryResponse response = new CategoryResponse();
        response.setCategoryName(category.getCategoryName());
        response.setDefaultUnit(category.getDefaultUnit());
        return ResponseEntity.ok(response);
    }
}
