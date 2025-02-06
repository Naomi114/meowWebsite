package tw.com.ispan.service.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Category;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.ProductDTO;
import tw.com.ispan.dto.ProductRequest;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.repository.shop.CartItemRepository;
import tw.com.ispan.repository.shop.CategoryRepository;
import tw.com.ispan.repository.shop.InventoryItemRepository;
import tw.com.ispan.repository.shop.OrderItemRepository;
import tw.com.ispan.repository.shop.ProductImageRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.repository.shop.WishListRepository;
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
	private CategoryRepository categoryRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private WishListRepository wishlistRepository;

	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductTagService productTagService;

	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private NotificationService notificationService;

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

			// 先存 Product實體，確保有 ID；避免圖片雙向關聯映射對應到 transient 狀態而無法執行
			product = productRepository.save(product);

			// 處理商品圖片(1~5張)
			productImageService.processProductImage(product, filenames);

			Product savedProduct = productRepository.save(product);
			response.setSuccess(true);
			response.setProduct(new ProductDTO(savedProduct));
			response.setMessage("商品新增成功");
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("商品新增失敗: " + e.getMessage());
			e.printStackTrace(); // 紀錄詳細錯誤
		}
		return response;
	}

	// 一次修改多屬性
	public ProductResponse updateSingle(Integer productId, ProductRequest request, List<MultipartFile> images) {
		ProductResponse response = new ProductResponse();

		Optional<Product> productOpt = productRepository.findById(productId);
		if (productOpt.isPresent()) {
			Product product = productOpt.get();

			// 更新非空欄位，null的判斷，若not-null欄位未修改，可避免跳出錯誤訊息
			if (request.getProductName() != null) {
				product.setProductName(request.getProductName());
			}
			if (request.getDescription() != null) {
				product.setDescription(request.getDescription());
			}
			if (request.getOriginalPrice() != null) {
				product.setOriginalPrice(request.getOriginalPrice());
			}
			if (request.getSalePrice() != null) {
				product.setSalePrice(request.getSalePrice());
			}
			if (request.getStockQuantity() != null) {
				product.setStockQuantity(request.getStockQuantity());
			}
			if (request.getUnit() != null) {
				product.setUnit(request.getUnit());
			}
			if (request.getExpire() != null) {
				product.setExpire(request.getExpire());
			}

			// 更新類別
			if (request.getCategoryId() != null) {
				// 直接查詢 category 並設定
				Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
				if (categoryOpt.isPresent()) {
					product.setCategory(categoryOpt.get());

					// 若類別有 defaultUnit，且商品單位尚未設定，則同步更新
					if (categoryOpt.get().getDefaultUnit() != null && product.getUnit() == null) {
						product.setUnit(categoryOpt.get().getDefaultUnit());
					}
				} else {
					throw new IllegalArgumentException("類別 ID 不存在");
				}
			} else if (request.getCategoryName() != null) {
				// 如果提供的是 categoryName，則查找 categoryId
				Integer resolvedCategoryId = categoryService.findCategoryIdByName(request.getCategoryName());
				if (resolvedCategoryId != null) {
					Optional<Category> categoryOpt = categoryRepository.findById(resolvedCategoryId);
					if (categoryOpt.isPresent()) {
						product.setCategory(categoryOpt.get());
					}
				} else {
					throw new IllegalArgumentException("無效的商品類別名稱");
				}
			}

			// 更新標籤
			if (request.getTags() != null) {
				productTagService.processProductTags(product, request.getTags());
			}

			// 更新圖片
			if (images != null && !images.isEmpty()) {
				productImageService.processProductImage(product, images);
			}

			product.setUpdatedAt(LocalDateTime.now()); // 更新時間
			Product updatedProduct = productRepository.save(product);

			response.setSuccess(true);
			response.setProduct(new ProductDTO(updatedProduct));
			response.setMessage("商品更新成功");
		} else {
			response.setSuccess(false);
			response.setMessage("商品不存在");
		}

		return response;
	}

	// 單筆刪除
	@Transactional
	public ProductResponse deleteSingle(Integer productId) {
		ProductResponse response = new ProductResponse();
		Optional<Product> productOpt = productRepository.findById(productId);

		if (productOpt.isPresent()) {
			Product product = productOpt.get();

			// **(1) 若商品已在訂單內，則禁止刪除並通知管理員**
			if (orderItemRepository.existsByProduct(product)) {
				notificationService.notifyAdmin(
						"刪除商品失敗",
						"商品 [" + product.getProductName() + "] 已有會員下單，無法刪除。");
				response.setSuccess(false);
				response.setMessage("商品已被訂購，無法刪除");
				return response;
			}

			// **(2) 查詢受影響的會員**
			List<Member> affectedMembers = new ArrayList<>();

			// 取得購物車內的會員
			List<Member> cartMembers = cartItemRepository.findMembersByProduct(product);
			affectedMembers.addAll(cartMembers);

			// 取得願望清單內的會員
			List<Member> wishlistMembers = wishlistRepository.findMembersByProduct(product);
			affectedMembers.addAll(wishlistMembers);

			// **(3) 發送通知給會員**
			for (Member member : affectedMembers) {
				notificationService.notifyMember(
						member,
						"商品已下架",
						"您購物車或願望清單內的商品 [" + product.getProductName() + "] 已被刪除，請更新您的購物車或願望清單。");
			}

			// **(4) 刪除關聯數據**
			wishlistRepository.deleteByProduct(product);
			cartItemRepository.deleteByProduct(product);
			productImageRepository.deleteByProduct(product);
			product.getTags().clear();

			// **(5) 刪除商品**
			productRepository.delete(product);
			productRepository.flush();

			response.setSuccess(true);
			response.setMessage("商品刪除成功，已通知相關會員");
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

	// 單筆查詢
	public ProductResponse findSingle(Integer productId) {
		ProductResponse response = new ProductResponse();

		Optional<Product> productOpt = productRepository.findById(productId);
		if (productOpt.isPresent()) {
			response.setSuccess(true);
			response.setProduct(new ProductDTO(productOpt.get()));
			response.setMessage("查詢成功");
		} else {
			response.setSuccess(false);
			response.setMessage("商品不存在");
		}
		return response;
	}

	// 動態多條件查詢: Specification類的應用
	public ProductResponse findBatch(String query, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
		ProductResponse response = new ProductResponse();

		try {
			Specification<Product> spec = Specification.where(null);

			if (query != null && !query.trim().isEmpty()) {
				spec = spec.and(ProductSpecifications.hasProductName(query));
			}
			if (minPrice != null && maxPrice != null) {
				spec = spec.and(ProductSpecifications.priceBetween(minPrice, maxPrice));
			}
			if (categoryId != null) {
				Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
				if (categoryOpt.isPresent()) {
					spec = spec.and(ProductSpecifications.hasCategory(categoryOpt.get()));
				} else {
					response.setSuccess(false);
					response.setMessage("無效的類別 ID");
					return response;
				}
			}

			List<Product> products = productRepository.findAll(spec);
			List<ProductDTO> productDTOs = products.stream().map(ProductDTO::new).toList();

			response.setSuccess(!productDTOs.isEmpty());
			response.setProducts(productDTOs);
			response.setMessage(productDTOs.isEmpty() ? "未找到符合條件的商品" : "搜尋成功");
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("搜尋失敗: " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// 查詢所有商品 (by Mark)
	public ProductResponse findAll() {
		ProductResponse response = new ProductResponse();
		List<Product> products = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : products) {
			productDTOs.add(new ProductDTO(product));
		}

		response.setSuccess(!products.isEmpty());
		response.setProducts(productDTOs);
		response.setMessage(products.isEmpty() ? "未找到任何商品" : "商品查詢成功");

		return response;
	}

	// 分頁
	public Page<Product> getAllPaged(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

}
