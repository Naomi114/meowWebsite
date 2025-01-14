package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.shop.CategoryBean;
import tw.com.ispan.domain.shop.InventoryItem;
import tw.com.ispan.domain.shop.ProductBean;
import tw.com.ispan.repository.shop.ProductRepository;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	// 商品增刪修
	public ProductBean create(String json) {
		try {
			JSONObject obj = new JSONObject(json);

			String productName = obj.isNull("productName") ? null : obj.getString("productName");
			String description = obj.isNull("description") ? null : obj.getString("description");
			BigDecimal originalPrice = obj.isNull("originalPrice") ? null : obj.getBigDecimal("originalPrice");
			BigDecimal salePrice = obj.isNull("salePrice") ? null : obj.getBigDecimal("salePrice");
			Integer stockQuantity = obj.isNull("stockQuantity") ? null : obj.getInt("stockQuantity");
			String unit = obj.isNull("unit") ? null : obj.getString("unit");
			LocalDate expire = obj.isNull("expire") ? null
					: LocalDate.parse(obj.getString("expire")); // 將字串解析為 LocalDate
			LocalDateTime createdAt = LocalDateTime.now();
			LocalDateTime updatedAt = LocalDateTime.now();
			Integer categoryId = obj.isNull("categoryId") ? null : obj.getInt("categoryId");
			Integer adminId = obj.isNull("adminId") ? null : obj.getInt("adminId");

			CategoryBean category = new CategoryBean();
			category.setCategoryId(categoryId);

			Admin admin = new Admin();
			admin.setAdminId(adminId);

			ProductBean product = new ProductBean();

			if (product.getStockQuantity() <= 0) {
				product.setStatus("已售完");
			} else {
				product.setStatus("上架中");
			}

			product.setProductName(productName);
			product.setDescription(description);
			product.setOriginalPrice(originalPrice);
			product.setSalePrice(salePrice);
			product.setStockQuantity(stockQuantity);
			product.setUnit(unit);
			product.setExpire(expire);
			product.setCreatedAt(createdAt);
			product.setUpdatedAt(updatedAt);
			product.setCategory(category);
			product.setAdmin(admin);

			return productRepository.save(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ProductBean insert(ProductBean bean) {
		if (bean != null && bean.getProductId() != null) {
			if (!productRepository.existsById(bean.getProductId())) {
				if (bean.getStockQuantity() <= 0) {
					bean.setStatus("已售完");
				} else {
					bean.setStatus("上架中");
				}
				return productRepository.save(bean);
			}
		}
		return null;
	}

	public ProductBean modify(ProductBean bean) {
		if (bean != null && bean.getProductId() != null) {
			if (productRepository.existsById(bean.getProductId())) {
				if (bean.getStockQuantity() <= 0) {
					bean.setStatus("已售完");
				} else {
					bean.setStatus("上架中");
				}
				return productRepository.save(bean);
			}
		}
		return null;
	}

	public boolean delete(ProductBean bean) {
		if (bean != null && bean.getProductId() != null) {
			// 確認商品是否存在
			if (productRepository.existsById(bean.getProductId())) {
				// 獲取商品實體，若不存在則拋出 NoSuchElementException
				ProductBean product = productRepository.findById(bean.getProductId()).orElseThrow();

				// 解除與盤點紀錄的關聯
				Set<InventoryItem> inventoryItems = product.getInventoryItems();
				inventoryItems.forEach(item -> item.setProduct(null)); // 將外鍵設為 NULL

				// 刪除商品
				productRepository.delete(product);
				return true;
			}
		}
		return false;
	}

	// 商品搜尋頁>>商品查詢: 品名或是描述模糊查詢
	public ProductBean findById(Integer id) {
		if (id != null) {
			Optional<ProductBean> optional = productRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public boolean exists(Integer id) {
		if (id != null) {
			return productRepository.existsById(id);
		}
		return false;
	}

	public List<ProductBean> select(ProductBean bean) {
		List<ProductBean> result = null;
		if (bean != null && bean.getProductId() != null && !bean.getProductId().equals(0)) {
			Optional<ProductBean> optional = productRepository.findById(bean.getProductId());
			if (optional.isPresent()) {
				result = new ArrayList<ProductBean>();
				result.add(optional.get());
			}
		} else {
			result = productRepository.findAll();
		}
		return result;
	}

	// 統計商品數量
	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return productRepository.count(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<ProductBean> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return productRepository.find(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
