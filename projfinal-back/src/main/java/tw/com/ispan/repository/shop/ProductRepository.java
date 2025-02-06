package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    boolean existsByProductName(String productName);

    // 查詢某類別下的所有商品
    List<Product> findByCategory(Category category);

    // 查詢多個類別下的所有商品
    List<Product> findByCategoryIn(List<Category> categories);

    // 分頁
    Page<Product> findAll(Pageable pageable);
}
