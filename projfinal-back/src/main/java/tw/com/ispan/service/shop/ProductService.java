package tw.com.ispan.service.shop;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.ProductBean;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.specification.ProductSpecifications;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	// 單筆刪除商品
	public ProductResponse deleteSingle(Integer productId) {
		ProductResponse response = new ProductResponse();

		Optional<ProductBean> productOpt = productRepository.findById(productId);
		if (productOpt.isPresent()) {
			productRepository.delete(productOpt.get());
			response.setSuccess(true);
			response.setMessage("商品刪除成功");
		} else {
			response.setSuccess(false);
			response.setMessage("商品不存在");
		}

		return response;
	}

	// 批量刪除商品
	public ProductResponse deleteBatch(List<Integer> productIds) {
		ProductResponse response = new ProductResponse();

		List<ProductBean> products = productRepository.findAllById(productIds);
		if (!products.isEmpty()) {
			productRepository.deleteAll(products);
			response.setSuccess(true);
			response.setMessage("批量刪除成功");
		} else {
			response.setSuccess(false);
			response.setMessage("未找到任何匹配的商品");
		}

		return response;
	}

	// 單筆修改商品
	public ProductResponse updateSingle(Integer productId, ProductRequest request) {
		ProductResponse response = new ProductResponse();

		Optional<ProductBean> productOpt = productRepository.findById(productId);
		if (productOpt.isPresent()) {
			ProductBean product = productOpt.get();
			product.setProductName(request.getProductName());
			product.setDescription(request.getDescription());
			product.setOriginalPrice(request.getOriginalPrice());
			product.setSalePrice(request.getSalePrice());
			product.setStockQuantity(request.getStockQuantity());
			product.setUnit(request.getUnit());
			product.setExpire(request.getExpire());
			ProductBean updatedProduct = productRepository.save(product);
			response.setSuccess(true);
			response.setProduct(updatedProduct);
			response.setMessage("商品更新成功");
		} else {
			response.setSuccess(false);
			response.setMessage("商品不存在");
		}

		return response;
	}

	// 批量修改商品
	public ProductResponse updateBatch(List<ProductRequest> requests) {
		ProductResponse response = new ProductResponse();

		// 遍歷請求列表並動態更新
		List<ProductBean> updatedProducts = requests.stream().map(request -> {
			// 動態查詢條件
			Specification<ProductBean> spec = Specification
					.where(ProductSpecifications.hasProductName(request.getProductName()));
			List<ProductBean> products = productRepository.findAll(spec);

			if (!products.isEmpty()) {
				ProductBean product = products.get(0); // 更新第一個匹配的商品
				product.setProductName(request.getProductName());
				product.setDescription(request.getDescription());
				product.setOriginalPrice(request.getOriginalPrice());
				product.setSalePrice(request.getSalePrice());
				product.setStockQuantity(request.getStockQuantity());
				product.setUnit(request.getUnit());
				product.setExpire(request.getExpire());
				return productRepository.save(product);
			}
			return null;
		}).filter(product -> product != null).collect(Collectors.toList());

		// 設定返回結果
		response.setSuccess(!updatedProducts.isEmpty());
		response.setProducts(updatedProducts);
		response.setMessage(updatedProducts.isEmpty() ? "未找到任何匹配的商品進行更新" : "批量更新成功");

		return response;
	}

	// 單筆查詢商品
	public ProductResponse findSingle(Integer productId) {
		ProductResponse response = new ProductResponse();

		Optional<ProductBean> productOpt = productRepository.findById(productId);
		if (productOpt.isPresent()) {
			response.setSuccess(true);
			response.setProduct(productOpt.get());
			response.setMessage("查詢成功");
		} else {
			response.setSuccess(false);
			response.setMessage("商品不存在");
		}

		return response;
	}

	// 批量查詢商品
	public ProductResponse findBatch(List<Integer> productIds) {
		ProductResponse response = new ProductResponse();

		List<ProductBean> products = productRepository.findAllById(productIds);
		response.setSuccess(!products.isEmpty());
		response.setProducts(products);
		response.setMessage(products.isEmpty() ? "未找到匹配的商品" : "批量查詢成功");

		return response;
	}
}
