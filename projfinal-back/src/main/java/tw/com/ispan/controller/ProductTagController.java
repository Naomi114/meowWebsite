package tw.com.ispan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import tw.com.ispan.dto.ProductTagRequest;
import tw.com.ispan.dto.ProductTagResponse;
import tw.com.ispan.service.shop.ProductTagService;

@RestController
@RequestMapping("/tags")
@CrossOrigin
public class ProductTagController {

    @Autowired
    private ProductTagService productTagService;

    // 新增標籤
    @PostMapping
    public ResponseEntity<ProductTagResponse> createTag(@RequestBody @Valid ProductTagRequest request) {
        ProductTagResponse response = productTagService.createTag(request);
        if (response == null) {
            throw new IllegalArgumentException("標籤新增失敗，返回值為空");
        }
        return ResponseEntity.ok(response);
    }

    // 修改標籤
    @PutMapping("/{id}")
    public ResponseEntity<ProductTagResponse> updateTag(@PathVariable Integer id,
            @RequestBody @Valid ProductTagRequest request) {
        ProductTagResponse response = productTagService.updateTag(id, request);
        if (response == null) {
            throw new IllegalArgumentException("標籤修改失敗，返回值為空");
        }
        return ResponseEntity.ok(response);
    }

    // 刪除標籤
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductTagResponse> deleteTag(@PathVariable Integer id) {
        ProductTagResponse response = productTagService.deleteTag(id);
        if (response == null) {
            throw new IllegalArgumentException("標籤刪除失敗，返回值為空");
        }
        return ResponseEntity.ok(response);
    }

    // 模糊查詢標籤
    @GetMapping("/search")
    public ResponseEntity<ProductTagResponse> searchTags(@RequestParam String keyword) {
        ProductTagResponse response = productTagService.searchTags(keyword);
        if (response == null) {
            throw new IllegalArgumentException("標籤查詢失敗，返回值為空");
        }
        return ResponseEntity.ok(response);
    }
}
