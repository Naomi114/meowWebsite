package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.domain.shop.ProductBean;
import tw.com.ispan.repository.shop.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * 單筆刪除商品
	 */
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

	/**
	 * 批量刪除商品
	 */
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

	/**
	 * 單筆修改商品
	 */
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

	/**
	 * 批量修改商品
	 */
	public ProductResponse updateBatch(List<ProductRequest> requests) {
		ProductResponse response = new ProductResponse();

		List<ProductBean> updatedProducts = requests.stream().map(request -> {
			Optional<ProductBean> productOpt = productRepository.findAll(request.getProductName());
			if (productOpt.isPresent()) {
				ProductBean product = productOpt.get();
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

		response.setSuccess(!updatedProducts.isEmpty());
		response.setProducts(updatedProducts);
		response.setMessage(updatedProducts.isEmpty() ? "未找到任何匹配的商品進行更新" : "批量更新成功");

		return response;
	}

	/**
	 * 單筆查詢商品
	 */
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

	/**
	 * 批量查詢商品
	 */
	public ProductResponse findBatch(List<Integer> productIds) {
		ProductResponse response = new ProductResponse();

		List<ProductBean> products = productRepository.findAllById(productIds);
		response.setSuccess(!products.isEmpty());
		response.setProducts(products);
		response.setMessage(products.isEmpty() ? "未找到匹配的商品" : "批量查詢成功");

		return response;
	}
}
