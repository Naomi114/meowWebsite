package tw.com.ispan.controller;

import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.service.shop.ProductService;
import tw.com.ispan.specification.ProductSpecifications;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 新增商品
    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequest request) {
        try {
            ProductResponse response = productService.createSingle(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("商品新增失敗: " + e.getMessage());
        }
    }

    // 修改商品
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateSingle(id, request);
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 刪除商品
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Integer id) {
        ProductResponse response = productService.deleteSingle(id);
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 查詢單個商品
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        ProductResponse response = productService.findSingle(id);
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 搜尋商品
    @PostMapping("/search")
    public ResponseEntity<ProductResponse> searchProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock) {

        // 動態構建 Specification 條件
        Specification<Product> spec = Specification.where(
                productName != null ? ProductSpecifications.hasProductName(productName) : null)
                .and(minPrice != null && maxPrice != null
                        ? ProductSpecifications.priceBetween(minPrice, maxPrice)
                        : null)
                .and(minStock != null && maxStock != null
                        ? ProductSpecifications.stockBetween(minStock, maxStock)
                        : null);

        // 調用 Service 層查詢方法
        ProductResponse response = productService.findBatch(spec);

        return ResponseEntity.ok(response);
    }

    // 搜尋所有商品 (by Mark)
    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse response = productService.findAll();
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
