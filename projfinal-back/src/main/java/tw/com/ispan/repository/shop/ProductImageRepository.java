package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

}
