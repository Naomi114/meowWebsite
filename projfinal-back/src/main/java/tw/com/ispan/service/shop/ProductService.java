package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.ProductImageRequest;
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
	public ProductResponse createSingle(ProductRequest request) {
		ProductResponse response = new ProductResponse();
		try {
			Product product = new Product();

			product.setProductName(request.getProductName());
			product.setDescription(request.getDescription());

			// 獲取類別，同時設定單位
			Category category = categoryService.findCategory(request.getCategoryName(), request.getUnit());
			product.setCategory(category);

			String unit = request.getUnit();
			if (unit == null || unit.isEmpty()) {
				unit = category.getDefaultUnit();
				if (unit == null || unit.isEmpty()) {
					throw new IllegalArgumentException("類別的預設單位未設置，無法保存商品");
				}
			}
			product.setUnit(unit);

			// 處理標籤 (可為0~N個標籤)
			productTagService.addTagsToProduct(product, request.getTags());

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

			product.setUnit(request.getUnit());

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

			/*
			 * ProductService 中處理以下圖片邏輯：
			 * 1. 檢查圖片數量是否為 1~5 張。
			 * 2. 驗證每張圖片的內容。
			 * 3. 儲存圖片並更新資料庫。
			 */

			if (request.getProductImages() == null || request.getProductImages().isEmpty()) {
				throw new IllegalArgumentException("商品圖片不能為空");
			}
			if (request.getProductImages().size() < 1 || request.getProductImages().size() > 5) {
				throw new IllegalArgumentException("商品圖片數量必須在 1 到 5 之間");
			}

			// 提取文件名列表 (轉換型別 List<ProductImageRequest> => List<String>)
			List<String> filenames = request.getProductImages().stream()
					.map(ProductImageRequest::getFilename)
					.collect(Collectors.toList());

			// 調用 ProductImageService 添加圖片
			productImageService.addProductImages(product, filenames);

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

	// 批量新增: 前端管理者頁面可以一次新增多個商品 (有空再做..)
	// public ProductResponse createBatch(List<ProductRequest> requests) {
	// ProductResponse response = new ProductResponse();
	// try {
	// Product product = new Product();
	// Category category = new Category();
	// product.setProductName(requests.getProductName());
	// product.setDescription(requests.getDescription());
	// category.setCategoryName(request.getCategoryName());
	// product.setCategory(category);
	// // isEmpty() 適用於 String, Collection, Map, Array 的空值判斷；日期轉成字串後也可以用
	// // 其中 String 型別因為 DTO 加入 @NotBlank、controller 加入 @Valid 驗證過，所以不會為空，無須再次驗證
	// if (request.getTags() == null || request.getTags().isEmpty()) {
	// throw new IllegalArgumentException("商品標籤不能為空");
	// }
	// List<ProductTag> tags = request.getTags().stream().map(tag -> {
	// ProductTag productTag = new ProductTag();
	// productTag.setTagName(tag.getTagName());
	// return productTag;
	// }).collect(Collectors.toList());
	// product.setTags(new HashSet<>(tags));
	// // compareTo 適用於 BigDecimal, BigInteger, Byte, Double, Integer, Long, Short
	// // 的空值判斷
	// if (request.getOriginalPrice() == null ||
	// request.getOriginalPrice().compareTo(null) == 0) {
	// throw new IllegalArgumentException("商品原價不能為空");
	// }
	// product.setOriginalPrice(request.getOriginalPrice());
	// if (request.getSalePrice() == null || request.getSalePrice().compareTo(null)
	// == 0) {
	// throw new IllegalArgumentException("商品售價不能為空");
	// }
	// product.setSalePrice(request.getSalePrice());
	// if (request.getStockQuantity() == null || request.getStockQuantity() == 0) {
	// throw new IllegalArgumentException("商品售價不能為空");
	// }
	// product.setStockQuantity(request.getStockQuantity());
	// product.setUnit(request.getUnit());
	// if (request.getStockQuantity() == 0) {
	// product.setStatus("已售完");
	// } else {
	// product.setStatus("上架中");
	// }
	// if (request.getExpire() == null || request.getExpire().toString().isEmpty())
	// {
	// throw new IllegalArgumentException("商品到期日不能為空");
	// }
	// product.setExpire(request.getExpire());
	// // 前端傳入多張圖片url，轉換成商品圖片實體
	// if (request.getProductImages() == null ||
	// request.getProductImages().isEmpty()) {
	// throw new IllegalArgumentException("商品圖片不能為空");
	// }
	// List<ProductImage> images = request.getProductImages().stream()
	// .filter(image -> image.getImageUrl() != null &&
	// !image.getImageUrl().trim().isEmpty())
	// .map(image -> {
	// ProductImage productImage = new ProductImage();
	// productImage.setImageUrl(image.getImageUrl().trim());
	// productImage.setProduct(product); // 設置雙向關係
	// return productImage;
	// }).collect(Collectors.toList());
	// product.setProductImages(new LinkedHashSet<>(images));
	// List<Product> savedProducts = productRepository.saveAll(products);
	// response.setSuccess(true);
	// response.setProducts(savedProducts);
	// response.setMessage("批量新增成功");
	// } catch (Exception e) {
	// response.setSuccess(false);
	// response.setMessage("批量新增失敗: " + e.getMessage());
	// }
	// return response;
	// }

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

	// 動態查詢: Specification類的應用
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
}
