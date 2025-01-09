package tw.com.ispan.projfinal_back.repository.shop;

import java.util.List;

import org.json.JSONObject;

import tw.com.ispan.projfinal_back.domain.shop.ProductBean;

public interface ProductDAO {
	public abstract Long count(JSONObject param);

	public abstract List<ProductBean> find(JSONObject param);
}