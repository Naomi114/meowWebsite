package tw.com.ispan.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.service.shop.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // 分頁
    @GetMapping("/paged")
    public ResponseEntity<Page<Product>> getProductsPaged(
            @RequestParam(defaultValue = "0") int page, // 預設第 0 頁
            @RequestParam(defaultValue = "10") int size // 預設每頁 10 筆
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllPaged(pageable);
        return ResponseEntity.ok(products);
    }

    // 新增商品
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("productRequest") String productRequestJson, // 先以 String 接收 JSON
            @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages) {

        try {
            // **解析 JSON**
            ProductRequest productRequest = objectMapper.readValue(productRequestJson, ProductRequest.class);

            // **呼叫 Service 層來處理**
            ProductResponse response = productService.createSingle(productRequest, productImages);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("商品新增失敗: " + e.getMessage());
        }
    }

    // 修改商品
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @RequestPart("productRequest") String productRequestJson,
            @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages) {

        try {
            ProductRequest productRequest = objectMapper.readValue(productRequestJson, ProductRequest.class);
            ProductResponse response = productService.updateSingle(id, productRequest, productImages);
            return response.getSuccess() ? ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ProductResponse(false, "更新失敗: " + e.getMessage()));
        }
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

    // 搜尋商品:商品關鍵字+價格區間+類別
    @GetMapping("/search")
    public ResponseEntity<ProductResponse> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        ProductResponse response = productService.findBatch(query, categoryId, minPrice, maxPrice);
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
