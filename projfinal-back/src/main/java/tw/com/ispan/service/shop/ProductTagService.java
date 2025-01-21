package tw.com.ispan.service.shop;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductTag;
import tw.com.ispan.dto.ProductTagRequest;
import tw.com.ispan.repository.shop.ProductTagRepository;

@Service
@Transactional
public class ProductTagService {

    @Autowired
    private ProductTagRepository productTagRepository;

    // 商品上架時，新增標籤
    public void addTagsToProduct(Product product, Set<ProductTagRequest> newTags) {
        if (newTags == null || newTags.isEmpty()) {
            if (product.getTags() == null) {
                product.setTags(new HashSet<>()); // 初始化為空集合
            }
            return; // 無標籤，不做處理
        }

        if (product.getTags() == null) {
            product.setTags(new HashSet<>()); // 初始化標籤集合
        }

        for (ProductTagRequest tagRequest : newTags) {
            if (tagRequest == null || tagRequest.getTagName() == null || tagRequest.getTagName().isBlank()) {
                continue; // 跳過無效標籤
            }

            ProductTag tag = productTagRepository.findByTagName(tagRequest.getTagName())
                    .orElseGet(() -> {
                        ProductTag newTag = new ProductTag();
                        newTag.setTagName(tagRequest.getTagName());
                        newTag.setTagDescription(tagRequest.getTagDescription());
                        return productTagRepository.save(newTag);
                    });

            product.getTags().add(tag); // 添加標籤到集合
        }
    }

    // 商品上架後，更新標籤
    public void updateProductTags(Product product, Set<ProductTagRequest> newTags) {
        if (product.getTags() == null) {
            product.setTags(new HashSet<>()); // 初始化集合
        }

        product.getTags().clear(); // 清空舊的標籤
        for (ProductTagRequest tagRequest : newTags) {
            ProductTag newTag = new ProductTag();
            newTag.setTagName(tagRequest.getTagName());
            newTag.setTagDescription(tagRequest.getTagDescription());
            product.getTags().add(newTag);
        }
    }

    /**
     * 查找或創建標籤
     *
     * @param tagName     標籤名稱
     * @param description 標籤描述
     * @return ProductTag
     */
    public ProductTag findOrCreateTag(String tagName, String description) {
        if (tagName == null || tagName.isBlank()) {
            throw new IllegalArgumentException("標籤名稱不能為空");
        }

        return productTagRepository.findByTagName(tagName).orElseGet(() -> {
            ProductTag newTag = new ProductTag();
            newTag.setTagName(tagName);
            newTag.setTagDescription(description);
            return productTagRepository.save(newTag);
        });
    }
}
