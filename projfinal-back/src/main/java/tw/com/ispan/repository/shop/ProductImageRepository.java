package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.ProductImage;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Integer>, JpaSpecificationExecutor<ProductImage> {

    void deleteByProduct(Product product);

}
