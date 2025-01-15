package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.ProductTag;

public interface TagRepository extends JpaRepository<ProductTag, Integer> {

}
