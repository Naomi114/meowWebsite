package tw.com.ispan.service.shop;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductTag;
import tw.com.ispan.dto.ProductTagRequest;
import tw.com.ispan.repository.shop.TagRepository;

@Service
@Transactional
public class ProductTagService {

    @Autowired
    private TagRepository tagRepository;

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

            ProductTag tag = tagRepository.findByTagName(tagRequest.getTagName())
                    .orElseGet(() -> {
                        ProductTag newTag = new ProductTag();
                        newTag.setTagName(tagRequest.getTagName());
                        newTag.setTagDescription(tagRequest.getTagDescription());
                        return tagRepository.save(newTag);
                    });

            product.getTags().add(tag); // 添加標籤到集合
        }
    }

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
}
