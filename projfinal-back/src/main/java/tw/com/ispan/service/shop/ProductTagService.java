package tw.com.ispan.service.shop;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductTag;
import tw.com.ispan.dto.shop.CategoryResponse;
import tw.com.ispan.dto.shop.ProductDTO;
import tw.com.ispan.dto.shop.ProductRequest;
import tw.com.ispan.dto.shop.ProductTagRequest;
import tw.com.ispan.dto.shop.ProductTagResponse;
import tw.com.ispan.repository.shop.ProductTagRepository;

@Service
@Transactional
public class ProductTagService {

    @Autowired
    private ProductTagRepository productTagRepository;

    // 新增標籤
    public ProductTagResponse createTag(ProductTagRequest request) {
        ProductTagResponse response = new ProductTagResponse();
        try {
            Optional<ProductTag> existingTag = productTagRepository.findByTagName(request.getTagName());
            if (existingTag.isPresent()) {
                throw new IllegalArgumentException("標籤已存在: " + request.getTagName());
            }

            ProductTag tag = new ProductTag();
            tag.setTagName(request.getTagName());
            tag.setTagDescription(request.getTagDescription());
            productTagRepository.save(tag);

            response.setSuccess(true);
            response.setMessage("標籤新增成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("標籤新增失敗: " + e.getMessage());
        }
        return response;
    }

    // 修改標籤
    public ProductTagResponse updateTag(Integer id, ProductTagRequest request) {
        ProductTagResponse response = new ProductTagResponse();
        try {
            ProductTag tag = productTagRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("標籤不存在: ID = " + id));
            tag.setTagName(request.getTagName());
            tag.setTagDescription(request.getTagDescription());
            productTagRepository.save(tag);

            response.setSuccess(true);
            response.setMessage("標籤修改成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("標籤修改失敗: " + e.getMessage());
        }
        return response;
    }

    // 刪除標籤
    public ProductTagResponse deleteTag(Integer id) {
        ProductTagResponse response = new ProductTagResponse();
        try {
            ProductTag tag = productTagRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("標籤不存在: ID = " + id));
            productTagRepository.delete(tag);

            response.setSuccess(true);
            response.setMessage("標籤刪除成功");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("標籤刪除失敗: " + e.getMessage());
        }
        return response;
    }

    // 模糊查詢標籤
    public ProductTagResponse searchTags(String keyword) {
        ProductTagResponse response = new ProductTagResponse();
        try {
            List<ProductTag> tags = productTagRepository.findByTagNameContaining(keyword);
            response.setSuccess(true);
            response.setTags(tags);
            response.setCount((long) tags.size());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("標籤查詢失敗: " + e.getMessage());
        }
        return response;
    }

    // 商品上架，處理標籤
    public void processProductTags(Product product, Set<ProductTagRequest> tagRequests) {
        if (tagRequests == null || tagRequests.isEmpty()) {
            return; // 如果標籤列表為空，直接返回
        }

        // 過濾重複的標籤名稱
        Set<String> uniqueTagNames = tagRequests.stream()
                .map(ProductTagRequest::getTagName)
                .filter(tagName -> tagName != null && !tagName.isEmpty())
                .collect(Collectors.toSet());

        // 遍歷標籤名稱，新增或關聯標籤
        for (String tagName : uniqueTagNames) {
            ProductTag tag = findOrCreateTag(tagName);
            product.addTag(tag); // 假設 Product 中有 addTag 方法
        }
    }

    // 尋找或建立標籤
    public ProductTag findOrCreateTag(String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            throw new IllegalArgumentException("標籤名稱不能為空");
        }

        // 查找是否已存在
        Optional<ProductTag> existingTag = productTagRepository.findByTagName(tagName);
        if (existingTag.isPresent()) {
            return existingTag.get();
        }

        // 不存在則創建
        ProductTag newTag = new ProductTag();
        newTag.setTagName(tagName);
        productTagRepository.save(newTag);
        return newTag;
    }

    // 型別轉換: ProductRequest => ProductTagRequest
    public ProductTagRequest buildTagRequestFromProduct(ProductRequest productRequest) {
        ProductTagRequest tagRequest = new ProductTagRequest();
        tagRequest.setTagName(productRequest.getTags().stream()
                .map(ProductTagRequest::getTagName)
                .findFirst()
                .orElse(null));
        tagRequest.setTagDescription(productRequest.getTagDescription());
        return tagRequest;
    }

    // 獲取所有標籤
    public ProductTagResponse getAllTags() {
        ProductTagResponse response = new ProductTagResponse();
        try {
            List<ProductTag> tags = productTagRepository.findAll();

            if (tags.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("沒有可用的類別");
                return response;
            }
            List<ProductDTO> productDTOs = tags.stream()
                    .flatMap(tag -> tag.getProducts().stream()) // 取得所有產品
                    .map(ProductDTO::new) // 轉換為 DTO，確保包含 images
                    .collect(Collectors.toList());

            response.setSuccess(true);
            response.setMessage("成功獲取標籤清單");
            response.setTags(tags);
            response.setProducts(productDTOs);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("獲取標籤失敗: " + e.getMessage());
        }
        return response;
    }
}
