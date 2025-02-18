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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/products")
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
    // this.productService = productService;
    // }

    // 分頁及價格排序
    @GetMapping("/paged")
    public ResponseEntity<?> getPagedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productName") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
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

    /*
     * 修改商品
     * 
     * @ModelAttribute 支援 multipart/form-data，並且可以解析 JSON + 圖片
     * 但 @PutMapping 不適合解析 @ModelAttribute，因為 PUT 請求通常是 application/json
     * ==> 解法: 將修改圖片和其他欄位拆成兩支 api
     */

    // @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity<ProductResponse> updateSingle(
    // @PathVariable("id") Integer productId,
    // @ModelAttribute ProductRequest request,
    // @RequestParam(value = "images", required = false) List<MultipartFile> images)
    // {

    // ProductResponse response = productService.updateSingle(productId, request,
    // images);
    // return ResponseEntity.ok(response);
    // }

    // 修改: 除了圖片欄位以外，的所有欄位
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> updateProductInfo(
            @PathVariable("id") Integer productId,
            @RequestBody ProductRequest request) { // ✅ 使用 JSON 格式

        ProductResponse response = productService.updateSingle(productId, request);
        return ResponseEntity.ok(response);
    }

    // 修改: 圖片欄位
    @PatchMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> updateProductImages(
            @PathVariable("id") Integer productId,
            @RequestParam("images") List<MultipartFile> images) { // ✅ 只處理圖片

        ProductResponse response = productService.updateProductImages(productId, images);
        return ResponseEntity.ok(response);
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
    // // ✅ 使用 Service 查詢 `List<Product>`
    // List<Product> filteredProducts = productService.findProductsByFilter(filter);

    // // ✅ 轉換 `List<Product>` 為 `List<ProductDTO>`
    // List<ProductDTO> productDTOs = filteredProducts.stream()
    // .map(ProductDTO::new) // ✅ 使用 `ProductDTO` 建構子進行轉換
    // .collect(Collectors.toList());

    // return ProductResponse.ok(productDTOs);
    // }

    // @PostMapping("/search")
    // public ProductResponse findBatch(@RequestBody ProductFilter filter) {
    // List<ProductDTO> filteredProducts =
    // productService.findProductsByFilter(filter);
    // return new ProductResponse(filteredProducts); // ✅ 確保回傳前端 { products: [...] }
    // }

    // 搜尋商品:商品關鍵字+價格區間+類別+標籤
    @PostMapping("/search")
    public ResponseEntity<ProductResponse> findBatch(@RequestBody ProductFilter filter) {
        List<ProductDTO> filteredProducts = productService.findProductsByFilter(filter);

        if (filteredProducts.isEmpty()) {
            return ResponseEntity.ok(new ProductResponse(false, "未找到符合條件的商品"));
        }

        return ResponseEntity.ok(new ProductResponse(true, "查詢成功", filteredProducts));
    }

    // 查詢所有商品: 改為傳回 ProductDTO格式(包含類別和標籤陣列) by Noami
    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

}
