package tw.com.ispan.controller.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.shop.ProductDTO;
import tw.com.ispan.dto.shop.ProductFilter;
import tw.com.ispan.dto.shop.ProductRequest;
import tw.com.ispan.dto.shop.ProductResponse;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.service.shop.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    // public ProductController(ProductService productService) {
    //     this.productService = productService;
    // }

    // 分頁及價格排序
    @GetMapping("/paged")
    public ResponseEntity<?> getPagedProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "productName") String sortBy,
        @RequestParam(defaultValue = "asc") String order
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortBy));
        Page<Product> productPage = productRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", productPage.getContent());
        response.put("totalPages", productPage.getTotalPages());
        response.put("totalElements", productPage.getTotalElements());
        response.put("pageable", productPage.getPageable()); // ✅ 確保 `pageable` 存在

        return ResponseEntity.ok(response);
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

    // 搜尋商品:商品關鍵字+價格區間+類別+標籤
    // 配合前端修改:GET 請求通常用於獲取資料，若包含大量參數，應考慮改用 POST
    // 使用 List<ProductDTO> 來封裝回應 → 確保前端可以接收多個商品
    // @PostMapping("/search")
    // public ProductResponse findBatch(@RequestBody ProductFilter filter) {
    //     // ✅ 使用 Service 查詢 `List<Product>`
    //     List<Product> filteredProducts = productService.findProductsByFilter(filter);

    //     // ✅ 轉換 `List<Product>` 為 `List<ProductDTO>`
    //     List<ProductDTO> productDTOs = filteredProducts.stream()
    //         .map(ProductDTO::new)  // ✅ 使用 `ProductDTO` 建構子進行轉換
    //         .collect(Collectors.toList());

    //     return ProductResponse.ok(productDTOs);
    // }

    @PostMapping("/search")
    public ProductResponse findBatch(@RequestBody ProductFilter filter) {
        List<ProductDTO> filteredProducts = productService.findProductsByFilter(filter);
        return new ProductResponse(filteredProducts); // ✅ 確保回傳前端 { products: [...] }
    }

    // @PostMapping("/search")
    // public ProductResponse findBatch(@RequestBody ProductFilter filter) {
    //     ProductResponse response = new ProductResponse();
        
    //     // ✅ 調用 Service 層來處理篩選
    //     List<Product> filteredProducts = productService.searchProducts(filter);
        
    //     response.setProductfilter(filteredProducts);
    //     return response;
    // }


    // 搜尋所有商品 (by Mark)
    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse response = productService.findAll();
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
