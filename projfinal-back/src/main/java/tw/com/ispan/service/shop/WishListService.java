package tw.com.ispan.service.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.WishList;
import tw.com.ispan.dto.shop.WishListRequest;
import tw.com.ispan.dto.shop.WishListResponse;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.repository.shop.WishListRepository;

@Service
@Transactional
public class WishListService {

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProductRepository productRepository;

	// 新增商品到願望清單
	public WishListResponse addWishList(WishListRequest request) {
		WishListResponse response = new WishListResponse();
		try {
			// 驗證會員是否存在
			Member member = memberRepository.findById(request.getMemberId())
					.orElseThrow(() -> new IllegalArgumentException("會員不存在"));

			// 驗證商品是否存在
			Product product = productRepository.findById(request.getProductId())
					.orElseThrow(() -> new IllegalArgumentException("商品不存在"));

			// 檢查是否已存在於願望清單
			if (wishListRepository.existsByMemberAndProduct(member, product)) {
				response.setSuccess(false);
				response.setMessage("該商品已存在於願望清單中");
				return response;
			}

			// 建立願望清單實體
			WishList wishList = new WishList();
			wishList.setMember(member); // 設置會員
			wishList.setProduct(product); // 設置商品
			wishList.setAddedAt(LocalDateTime.now()); // 設置加入時間

			// 儲存願望清單
			wishListRepository.save(wishList);

			// 回應成功結果
			response.setSuccess(true);
			response.setMessage("商品已成功加入願望清單");
		} catch (Exception e) {
			// 回應失敗結果
			response.setSuccess(false);
			response.setMessage("商品加入願望清單失敗: " + e.getMessage());
		}
		return response;
	}

	// 從收藏清單移除商品
	public WishListResponse removeWishList(WishListRequest request) {
		WishListResponse response = new WishListResponse();
		try {
			Member member = memberRepository.findById(request.getMemberId())
					.orElseThrow(() -> new IllegalArgumentException("會員不存在"));

			Product product = productRepository.findById(request.getProductId())
					.orElseThrow(() -> new IllegalArgumentException("商品不存在"));

			WishList wishList = wishListRepository.findByMemberAndProduct(member, product)
					.orElseThrow(() -> new IllegalArgumentException("該商品不在願望清單中"));

			wishListRepository.delete(wishList);
			response.setSuccess(true);
			response.setMessage("商品已成功從願望清單移除");
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("商品移除失敗: " + e.getMessage());
		}
		return response;
	}

	// 會員查詢收藏清單
	public WishListResponse findAllWishListsByMember(Integer memberId) {
		WishListResponse response = new WishListResponse();
		try {
			// 驗證會員是否存在
			Member member = memberRepository.findById(memberId)
					.orElseThrow(() -> new IllegalArgumentException("會員不存在"));

			// 查詢會員的所有願望清單
			List<WishList> wishLists = wishListRepository.findByMember(member);

			// 若清單為空，將 wishlists 設為 null
			if (wishLists == null || wishLists.isEmpty()) {
				response.setWishlists(null);
				response.setCount(null); // 無願望清單時 count 為 null
			} else {
				response.setWishlists(wishLists);
				response.setCount((long) wishLists.size()); // 設定願望清單的大小
			}

			response.setSuccess(true);

		} catch (Exception e) {
			// 異常處理：設置失敗訊息並將 wishlists 和 count 設為 null
			response.setSuccess(false);
			response.setWishlists(null);
			response.setCount(null);
			response.setMessage("查詢失敗: " + e.getMessage());
		}
		return response;
	}

}
