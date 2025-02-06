package tw.com.ispan.controller;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.com.ispan.domain.shop.CartActionLog;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.dto.CartItemResponse;
import tw.com.ispan.dto.CartRequest;
import tw.com.ispan.dto.CartResponse;
import tw.com.ispan.service.shop.CartActionLogService;
import tw.com.ispan.service.shop.CartService;

@RestController
@RequestMapping("/pages/cart")
@CrossOrigin
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CartActionLogService cartActionLogService;

    // ✅ 1. 創建購物車 (會員登入後建立)
    @PostMapping("/create")
    public ResponseEntity<Integer> createCart(@RequestBody Integer memberId) {
        try {
            Integer cartId = cartService.createCart(memberId).getCartId();
            return ResponseEntity.status(HttpStatus.CREATED).body(cartId);
        } catch (Exception e) {
            log.error("Error creating cart for memberId: " + memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ 2. 查詢購物車內容
    @GetMapping(value = "/list/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartItem>> getCartByMemberId(@PathVariable Integer memberId) {
        try {
            List<CartItem> cartItems = cartService.getCartItemsByMemberId(memberId);
            return cartItems.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(cartItems)
                    : ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            log.error("Error fetching cart items for memberId: " + memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ 3. 新增商品至購物車
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addCart(@RequestBody @Valid CartRequest request) {
        try {
            CartResponse response = cartService.addCartItem(request);
            return response.isSuccess() ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Error adding item to cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CartResponse(false, "Failed to add item to cart"));
        }
    }

    // ✅ 4. 刪除購物車內的單筆商品
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Integer cartItemId) {
        try {
            boolean removed = cartService.removeCartItem(cartItemId);
            return removed ? ResponseEntity.ok("Item removed from cart.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to remove item.");
        } catch (Exception e) {
            log.error("Error removing item from cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to remove item.");
        }
    }

    // ✅ 5. 清空購物車 (移除所有商品)
    @DeleteMapping("/clear/{memberId}")
    public ResponseEntity<String> clearCart(@PathVariable Integer memberId) {
        try {
            cartService.clearCartByMemberId(memberId);
            return ResponseEntity.ok("Cart cleared successfully.");
        } catch (Exception e) {
            log.error("Error clearing cart for memberId: " + memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to clear cart.");
        }
    }

    // ✅ 6. 更新購物車內商品數量
    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestBody CartItem cartItem) {
        try {
            boolean updated = cartService.updateCartItemQuantity(cartItem.getCartItemId(), cartItem.getQuantity());
            if (updated) {
                CartItem updatedCartItem = cartService.getCartItemById(cartItem.getCartItemId());
                CartItemResponse response = new CartItemResponse(updatedCartItem);
                return ResponseEntity.ok("Cart item updated successfully: " + response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to update cart item.");
            }
        } catch (Exception e) {
            log.error("Error updating cart item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to update cart item.");
        }
    }

    // ✅ 7. 查詢購物車操作行為紀錄 (透過 `Interceptor` 紀錄) by Naomi
    @GetMapping("/actions/{memberId}")
    public ResponseEntity<List<CartActionLog>> getCartActionLogs(@PathVariable Long memberId) {
        try {
            List<CartActionLog> logs = cartActionLogService.getLogsByMemberId(memberId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Error fetching cart action logs for memberId: " + memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
