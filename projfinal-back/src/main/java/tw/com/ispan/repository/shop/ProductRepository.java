package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.CategoryBean;
import tw.com.ispan.domain.shop.ProductBean;

public interface ProductRepository extends JpaRepository<ProductBean, Integer>, ProductDAO{
    // 商品搜尋頁>>商品名稱模糊查詢
    // 包含關鍵字
    List<CategoryBean> findByProductNameContaining(String keyword);

    // 以關鍵字開頭
    List<CategoryBean> findByProductNameStartingWith(String prefix);

    // 以關鍵字結尾
    List<CategoryBean> findByProductNameEndingWith(String suffix);
}
