package tw.com.ispan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.dto.WishListRequest;
import tw.com.ispan.dto.WishListResponse;
import tw.com.ispan.service.shop.WishListService;

@RestController
@RequestMapping("/wishlists")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    // 新增商品到願望清單
    @PostMapping
    public ResponseEntity<WishListResponse> addToWishList(@RequestBody @Valid WishListRequest request) {
        WishListResponse response = wishListService.addWishList(request);
        return ResponseEntity.ok(response);
    }

    // 從願望清單移除商品
    @DeleteMapping
    public ResponseEntity<WishListResponse> removeFromWishList(@RequestBody @Valid WishListRequest request) {
        WishListResponse response = wishListService.removeWishList(request);
        return ResponseEntity.ok(response);
    }

    // 模糊查詢願望清單
    @GetMapping("/search")
    public ResponseEntity<WishListResponse> searchWishList(
            @RequestParam Integer memberId,
            @RequestParam String productName) {
        WishListResponse response = wishListService.searchWishList(memberId, productName);
        return ResponseEntity.ok(response);
    }
}
