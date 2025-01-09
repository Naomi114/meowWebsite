package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.ProductBean;

public interface ProductRepository extends JpaRepository<ProductBean, Integer>, ProductDAO {

}
