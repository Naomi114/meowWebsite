package tw.com.ispan.repository.shop;

import java.util.List;

import org.json.JSONObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.shop.ProductBean;

// 存放 JpaRepository以外自訂的方法
public interface ProductDAO {
	public abstract Long count(JSONObject param);

	public abstract List<ProductBean> find(JSONObject param);

	// 模糊搜尋: 商品名稱或描述
	@Query("SELECT p FROM ProductBean p WHERE p.productName LIKE %:keyword% OR p.description LIKE %:keyword%")
	public abstract List<ProductBean> findByProductNameContainingOrDescriptionContaining(
			@Param("keyword") String keyword1,
			@Param("keyword") String keyword2);

}