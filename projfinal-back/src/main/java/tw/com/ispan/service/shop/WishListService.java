package tw.com.ispan.service.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.WishList;
import tw.com.ispan.dto.WishListRequest;
import tw.com.ispan.dto.WishListResponse;
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
			Member member = memberRepository.findById(request.getMemberId())
					.orElseThrow(() -> new IllegalArgumentException("會員不存在"));

			Product product = productRepository.findById(request.getProductId())
					.orElseThrow(() -> new IllegalArgumentException("商品不存在"));

			if (wishListRepository.existsByMemberAndProduct(member, product)) {
				throw new IllegalArgumentException("該商品已存在於願望清單中");
			}

			WishList wishList = new WishList();
			wishList.setMember(member);
			wishList.setProduct(product);
			wishList.setAddedAt(LocalDateTime.now());

			wishListRepository.save(wishList);
			response.setSuccess(true);
			response.setMessage("商品已成功加入願望清單");
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("商品加入願望清單失敗: " + e.getMessage());
		}
		return response;
	}

	// 從願望清單移除商品
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

	// 模糊查詢願望清單
	public WishListResponse searchWishList(Integer memberId, String productName) {
		WishListResponse response = new WishListResponse();
		try {
			Member member = memberRepository.findById(memberId)
					.orElseThrow(() -> new IllegalArgumentException("會員不存在"));

			List<WishList> wishLists = wishListRepository.findByMemberAndProductProductNameContaining(member,
					productName);
			response.setSuccess(true);
			response.setWishlists(wishLists);
			response.setCount((long) wishLists.size());
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("查詢失敗: " + e.getMessage());
		}
		return response;
	}
}
