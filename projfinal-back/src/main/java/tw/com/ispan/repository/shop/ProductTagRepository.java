package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.ProductTag;

public interface ProductTagRepository extends JpaRepository<ProductTag, Integer>, JpaSpecificationExecutor<ProductTag> {

    // 模糊查詢
    List<ProductTag> findByTagNameContaining(String keyword);

    // 精確查詢
    Optional<ProductTag> findByTagName(String tagName);
}
