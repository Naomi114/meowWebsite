package tw.com.ispan.projfinal_back.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.projfinal_back.domain.shop.ProductBean;

public interface ProductRepository extends JpaRepository<ProductBean, Integer>, ProductDAO {

}
