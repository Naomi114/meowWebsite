package tw.com.ispan.service.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.ProductTag;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductTagRequest;
import tw.com.ispan.dto.ProductTagResponse;
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

    // 多選標籤型別轉換: ProductRequest => ProductTagRequest
    public ProductTagRequest buildTagRequestFromProduct(ProductRequest productRequest) {
        ProductTagRequest tagRequest = new ProductTagRequest();
        tagRequest.setTagName(productRequest.getTags().stream()
                .map(ProductTagRequest::getTagName)
                .findFirst()
                .orElse(null));
        tagRequest.setTagDescription(productRequest.getTagDescription());
        return tagRequest;
    }

}
