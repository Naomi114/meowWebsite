package tw.com.ispan.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.repository.shop.CartItemRepository;

@Service
@Transactional
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    // 盤點購物車商品數量時，同步更新購物車數量 (20250114 by Naomi)
    public void syncCartQuantities(Integer productId, Integer newStockQuantity) {

        // 查找所有未下單的購物車項目
        List<CartItem> cartItems = cartItemRepository.findByProduct_ProductIdAndOrderIsNull(productId);

        // 更新購物車數量
        for (CartItem item : cartItems) {
            if (item.getCartItemQuantity() > newStockQuantity) {
                item.setCartItemQuantity(newStockQuantity); // 限制購物車數量為最新庫存
            }
        }

        cartItemRepository.saveAll(cartItems);
    }
}
