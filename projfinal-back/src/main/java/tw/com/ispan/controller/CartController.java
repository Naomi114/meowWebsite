package tw.com.ispan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tw.com.ispan.service.shop.CartService;
import tw.com.ispan.domain.shop.Cart;

@Controller
@RequestMapping("/pages/cart") // 修改路徑，避免與其他控制器衝突
@CrossOrigin  // 支持跨域
public class CartController {

    @Autowired
    private CartService cartService;

    // 顯示購物車中的所有商品
    @GetMapping("/list/{memberId}")
    @ResponseBody
    public List<Cart> getCartByMemberId(@PathVariable Integer memberId) {
        return cartService.getCartByMemberId(memberId);
    }

    // 新增商品到購物車
    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestBody Cart cart) {
        boolean result = cartService.addToCart(cart);
        if (result) {
            return "Item added to cart successfully.";
        } else {
            return "Error: Unable to add item to cart.";
        }
    }

    // 更新購物車中的商品數量
    @PutMapping("/update")
    @ResponseBody
    public String updateCart(@RequestBody Cart cart) {
        boolean updated = cartService.updateQuantity(cart.getCartId(), cart.getQuantity());
        if (updated) {
            return "Cart updated successfully.";
        } else {
            return "Error: Unable to update cart.";
        }
    }

    // 從購物車中刪除商品
    @DeleteMapping("/remove/{cartId}")
    @ResponseBody
    public String removeItem(@PathVariable Long cartId) {
        boolean removed = cartService.removeItem(cartId);
        if (removed) {
            return "Item removed from cart.";
        } else {
            return "Error: Unable to remove item.";
        }
    }
}
