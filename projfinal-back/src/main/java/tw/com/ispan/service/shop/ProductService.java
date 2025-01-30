package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.specification.ProductSpecifications;

/*
* isEmpty() 適用於 String, Collection, Map, Array 的空值判斷
* 日期轉成字串後也可以用
* 
	其中 String 型別因為
* 1. DTO 加入 @NotBlank、
* 2. controller 加入 @Valid 驗證過
* 所以不會為空，無須再次驗證

* isPresent() 適用於精確查詢 optional<> 的空值判斷
*/

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductTagService productTagService;

	@Autowired
	private ProductImageService productImageService;

	// 單筆新增
	public ProductResponse createSingle(ProductRequest request, List<MultipartFile> filenames) {
		ProductResponse response = new ProductResponse();
		try {
			Product product = new Product();

			product.setProductName(request.getProductName());
			product.setDescription(request.getDescription());

			// 處理類別(1種)
			categoryService.processCategory(product, Set.of(categoryService.buildCategoryRequestFromProduct(request)));

			// 處理標籤(0~N個)
			productTagService.processProductTags(product, request.getTags());

			// 處理商品圖片(1~5張)
			productImageService.processProductImage(product, filenames);

			/*
			 * compareTo 適用於以下型別的空值判斷
			 * BigDecimal, BigInteger, Byte, Double, Integer, Long, Short
			 * 
			 * 傳回null需要另外判斷，且先判斷是否為null，才能後續比較空值，不然會出現NullpointerException
			 */

			if (request.getOriginalPrice() == null) {
				throw new IllegalArgumentException("商品原價不能為空");
			}
			if (request.getOriginalPrice().compareTo(BigDecimal.ZERO) == 0) {
				throw new IllegalArgumentException("商品原價不能為零");
			}
			product.setOriginalPrice(request.getOriginalPrice());

			if (request.getSalePrice() == null) {
				throw new IllegalArgumentException("商品售價不能為空");
			}
			if (request.getSalePrice().compareTo(BigDecimal.ZERO) == 0) {
				throw new IllegalArgumentException("商品售價不能為零");
			}
			product.setSalePrice(request.getSalePrice());

			if (request.getStockQuantity() == null) {
				throw new IllegalArgumentException("商品數量不能為空");
			}
			if (request.getStockQuantity() == 0) {
				throw new IllegalArgumentException("商品數量不能為零");
			}
			product.setStockQuantity(request.getStockQuantity());

			if (request.getStockQuantity() == 0) {
				product.setStatus("已售完");
			} else {
				product.setStatus("上架中");
			}

			if (request.getExpire() == null || request.getExpire().toString().isEmpty()) {
				throw new IllegalArgumentException("商品到期日不能為空");
			}
			product.setExpire(request.getExpire());

			// 創建時間和更新時間自動生成
			product.setCreatedAt(LocalDateTime.now());
			product.setUpdatedAt(LocalDateTime.now());

			Product savedProduct = productRepository.save(product);
			response.setSuccess(true);
			response.setProduct(savedProduct);
			response.setMessage("商品新增成功");
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("商品新增失敗: " + e.getMessage());
			e.printStackTrace(); // 紀錄詳細錯誤
		}
		return response;
	}

	// 單筆刪除
	public ProductResponse deleteSingle(Integer productId) {
		ProductResponse response = new ProductResponse();

		Optional<Product> productOpt = productRepository.findById(productId);
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

	// 批量刪除
	public ProductResponse deleteBatch(List<Integer> productIds) {
		ProductResponse response = new ProductResponse();

		List<Product> products = productRepository.findAllById(productIds);
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

	// 單筆修改
	public ProductResponse updateSingle(Integer productId, ProductRequest request) {
		ProductResponse response = new ProductResponse();

		Optional<Product> productOpt = productRepository.findById(productId);
		if (productOpt.isPresent()) {
			Product product = productOpt.get();
			product.setProductName(request.getProductName());
			product.setDescription(request.getDescription());
			product.setOriginalPrice(request.getOriginalPrice());
			product.setSalePrice(request.getSalePrice());
			product.setStockQuantity(request.getStockQuantity());
			product.setUnit(request.getUnit());
			product.setExpire(request.getExpire());
			Product updatedProduct = productRepository.save(product);
			response.setSuccess(true);
			response.setProduct(updatedProduct);
			response.setMessage("商品更新成功");
		} else {
			response.setSuccess(false);
			response.setMessage("商品不存在");
		}

		return response;
	}

	// 批量修改
	public ProductResponse updateBatch(List<ProductRequest> requests) {
		ProductResponse response = new ProductResponse();

		// 遍歷請求列表並動態更新
		List<Product> updatedProducts = requests.stream().map(request -> {
			// 動態查詢條件
			Specification<Product> spec = Specification
					.where(ProductSpecifications.hasProductName(request.getProductName()));
			List<Product> products = productRepository.findAll(spec);

			if (!products.isEmpty()) {
				Product product = products.get(0); // 更新第一個匹配的商品
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

	// 單筆查詢
	public ProductResponse findSingle(Integer productId) {
		ProductResponse response = new ProductResponse();

		Optional<Product> productOpt = productRepository.findById(productId);
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

	// 動態多條件查詢: Specification類的應用
	public ProductResponse findBatch(Specification<Product> spec) {
		ProductResponse response = new ProductResponse();

		// 使用 Specification 執行查詢
		List<Product> products = productRepository.findAll(spec);

		// 設定返回結果
		response.setSuccess(!products.isEmpty());
		response.setProducts(products);
		response.setMessage(products.isEmpty() ? "未找到匹配的商品" : "批量查詢成功");

		return response;
	}

	// 查詢所有商品 (by Mark)
	public ProductResponse findAll() {
		ProductResponse response = new ProductResponse();
		List<Product> products = productRepository.findAll();

		response.setSuccess(!products.isEmpty());
		response.setProducts(products);
		response.setMessage(products.isEmpty() ? "未找到任何商品" : "商品查詢成功");

		return response;
	}

	

	
}
