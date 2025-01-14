package tw.com.ispan.repository.shop;

import java.util.List;

import org.json.JSONObject;

import tw.com.ispan.domain.shop.ProductBean;

// 存放 JpaRepository以外自訂的方法
public interface ProductDAO {
	public abstract Long count(JSONObject param);

	public abstract List<ProductBean> find(JSONObject param);

}