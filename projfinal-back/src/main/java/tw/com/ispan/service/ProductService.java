package tw.com.ispan.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductTag;
import tw.com.ispan.dto.ProductImageRequest;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.specification.ProductSpecifications;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageService productImageService;

    // Add method to get all products
    public ProductResponse findAll() {
        ProductResponse response = new ProductResponse();
        List<Product> products = productRepository.findAll();
        
        response.setSuccess(!products.isEmpty());
        response.setProducts(products);
        response.setMessage(products.isEmpty() ? "未找到任何商品" : "商品查詢成功");
        
        return response;
    }

    // 單筆新增
    public ProductResponse createSingle(ProductRequest request) {
        ProductResponse response = new ProductResponse();
        try {
            Product product = new Product();
            Category category = new Category();

            product.setProductName(request.getProductName());
            product.setDescription(request.getDescription());

            category.setCategoryName(request.getCategoryName());
            product.setCategory(category);

            if (request.getTags() == null || request.getTags().isEmpty()) {
                throw new IllegalArgumentException("商品標籤不能為空");
            }
            List<ProductTag> tags = request.getTags().stream().map(tag -> {
                ProductTag productTag = new ProductTag();
                productTag.setTagName(tag.getTagName());
                return productTag;
            }).collect(Collectors.toList());
            product.setTags(new HashSet<>(tags));

            if (request.getOriginalPrice() == null || request.getOriginalPrice().compareTo(null) == 0) {
                throw new IllegalArgumentException("商品原價不能為空");
            }
            product.setOriginalPrice(request.getOriginalPrice());

            if (request.getSalePrice() == null || request.getSalePrice().compareTo(null) == 0) {
                throw new IllegalArgumentException("商品售價不能為空");
            }
            product.setSalePrice(request.getSalePrice());

            if (request.getStockQuantity() == null || request.getStockQuantity() == 0) {
                throw new IllegalArgumentException("商品售價不能為空");
            }
            product.setStockQuantity(request.getStockQuantity());

            product.setUnit(request.getUnit());

            if (request.getStockQuantity() == 0) {
                product.setStatus("已售完");
            } else {
                product.setStatus("上架中");
            }

            if (request.getExpire() == null || request.getExpire().toString().isEmpty()) {
                throw new IllegalArgumentException("商品到期日不能為空");
            }
            product.setExpire(request.getExpire());

            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            if (request.getProductImages() == null || request.getProductImages().isEmpty()) {
                throw new IllegalArgumentException("商品圖片不能為空");
            }
            if (request.getProductImages().size() < 1 || request.getProductImages().size() > 5) {
                throw new IllegalArgumentException("商品圖片數量必須在 1 到 5 之間");
            }

            List<ProductImageRequest> productImageRequests = request.getProductImages().stream()
                    .map(image -> {
                        ProductImageRequest imageRequest = new ProductImageRequest();
                        imageRequest.setImageUrl(image.getImageUrl());
                        return imageRequest;
                    })
                    .collect(Collectors.toList());

            productImageService.addProductImages(product, productImageRequests);

            Product savedProduct = productRepository.save(product);
            response.setSuccess(true);
            response.setProduct(savedProduct);
            response.setMessage("商品新增成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("商品新增失敗: " + e.getMessage());
        }
        return response;
    }

    // 單筆查詢
    public ProductResponse findSingle(Integer productId) {
        ProductResponse response = new ProductResponse();
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            response.setSuccess(true);
            response.setProduct(productOpt.get());
            response.setMessage("查詢成功");
        } else {
            response.setSuccess(false);
            response.setMessage("商品不存在");
        }
        return response;
    }

    // 動態查詢: Specification類的應用
    public ProductResponse findBatch(Specification<Product> spec) {
        ProductResponse response = new ProductResponse();
        List<Product> products = productRepository.findAll(spec);
        response.setSuccess(!products.isEmpty());
        response.setProducts(products);
        response.setMessage(products.isEmpty() ? "未找到匹配的商品" : "批量查詢成功");
        return response;
    }

    // 刪除單筆商品資料
    public ProductResponse deleteSingle(Integer productId) {
        ProductResponse response = new ProductResponse();
        try {
            // 查詢商品
            Optional<Product> productOpt = productRepository.findById(productId);
            if (!productOpt.isPresent()) {
                response.setSuccess(false);
                response.setMessage("商品不存在");
                return response;
            }

            Product product = productOpt.get();

            // 刪除商品
            productRepository.delete(product);
            response.setSuccess(true);
            response.setMessage("商品刪除成功");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("商品刪除失敗: " + e.getMessage());
        }
        return response;
    }

    // 新增 updateSingle 方法: 更新單筆商品資料
    public ProductResponse updateSingle(Integer productId, ProductRequest request) {
        ProductResponse response = new ProductResponse();
        try {
            // 查詢商品
            Optional<Product> productOpt = productRepository.findById(productId);
            if (!productOpt.isPresent()) {
                response.setSuccess(false);
                response.setMessage("商品不存在");
                return response;
            }

            Product product = productOpt.get();

            // 更新商品資料
            product.setProductName(request.getProductName());
            product.setDescription(request.getDescription());
            product.setOriginalPrice(request.getOriginalPrice());
            product.setSalePrice(request.getSalePrice());
            product.setStockQuantity(request.getStockQuantity());
            product.setUnit(request.getUnit());
            product.setExpire(request.getExpire());
            product.setUpdatedAt(LocalDateTime.now());

            if (request.getTags() != null && !request.getTags().isEmpty()) {
                List<ProductTag> tags = request.getTags().stream().map(tag -> {
                    ProductTag productTag = new ProductTag();
                    productTag.setTagName(tag.getTagName());
                    return productTag;
                }).collect(Collectors.toList());
                product.setTags(new HashSet<>(tags));
            }

            // Update images if available
            if (request.getProductImages() != null && !request.getProductImages().isEmpty()) {
                List<ProductImageRequest> productImageRequests = request.getProductImages().stream()
                        .map(image -> {
                            ProductImageRequest imageRequest = new ProductImageRequest();
                            imageRequest.setImageUrl(image.getImageUrl());
                            return imageRequest;
                        })
                        .collect(Collectors.toList());
                productImageService.addProductImages(product, productImageRequests);
            }

            Product updatedProduct = productRepository.save(product);
            response.setSuccess(true);
            response.setProduct(updatedProduct);
            response.setMessage("商品更新成功");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("商品更新失敗: " + e.getMessage());
        }
        return response;
    }
}
