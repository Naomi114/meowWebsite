package tw.com.ispan.repository.shop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.ProductTag;

public interface TagRepository extends JpaRepository<ProductTag, Integer>, JpaSpecificationExecutor<ProductTag> {
    Optional<ProductTag> findByTagName(String tagName);
}
