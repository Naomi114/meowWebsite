package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.repository.shop.CartRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // 根據 memberId 獲取購物車資料
    public List<Cart> getCartByMemberId(Integer memberId) {
        return cartRepository.findByMember_memberId(memberId);  // 使用 findByMember_memberId 查詢
    }

    // 添加商品到購物車並更新資料庫
    public boolean addToCart(Cart cart) {
        // 檢查該商品是否已經在購物車中
        Cart existingCart = cartRepository.findByMemberAndProductId(cart.getMember(), cart.getProductId());
        if (existingCart != null) {
            // 如果商品已存在，則更新商品數量
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            cartRepository.save(existingCart); // 更新資料庫
        } else {
            // 若商品不存在，則新增商品到購物車
            cart.setLastUpdatedDate(LocalDateTime.now()); // 設置更新日期
            cartRepository.save(cart); // 新增商品到購物車
        }
        return true;
    }

    // 更新購物車商品數量
    public boolean updateQuantity(Long cartId, int quantity) {
        // 根據 cartId 查找購物車商品
        Optional<Cart> cartOptional = cartRepository.findById(cartId); // 使用 Optional 來處理可能為 null 的情況
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get(); // 取出 Cart 物件
            // 更新數量
            cart.setQuantity(quantity);
            cartRepository.save(cart); // 保存更新後的商品
            return true;
        }
        return false;  // 如果找不到商品，返回 false
    }

    // 從購物車中刪除商品
    public boolean removeItem(Long cartId) {
        // 確認是否存在該商品
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);  // 刪除商品
            return true;
        }
        return false;  // 如果商品不存在，返回 false
    }

    // 結帳並刪除已勾選的商品
    public void checkout(List<Long> cartIds) {
        // 根據 cartIds 刪除已勾選的商品
        cartRepository.deleteByCartIdIn(cartIds);  // 刪除指定 cartIds 的商品
    }

    // 根據 cartId 查找購物車商品 (新增此方法)
    public Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);  // 根據 cartId 查找購物車商品
    }

    // 更新購物車資料 (新增此方法)
    public void updateCart(Cart cart) {
        cartRepository.save(cart);  // 更新購物車資料
    }
}
