package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.ispan.dto.CartRequest;
import tw.com.ispan.dto.CartResponse;
import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.repository.shop.CartRepository;
import tw.com.ispan.repository.shop.ProductRepository;
import tw.com.ispan.repository.admin.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    // 根據 memberId 獲取購物車資料
    public List<Cart> getCartByMemberId(Integer memberId) {
        return cartRepository.findByMember_memberId(memberId); // 使用 findByMember_memberId 查詢
    }

    // 添加商品到購物車並更新資料庫
    public boolean addToCart(Cart cart) {
        // 檢查商品數量是否大於 0
        if (cart.getQuantity() <= 0) {
            return false; // 如果商品數量不合法，直接返回 false
        }

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
        return false; // 如果找不到商品，返回 false
    }

    // 從購物車中刪除商品
    public boolean removeItem(Long cartId) {
        // 確認是否存在該商品
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId); // 刪除商品
            return true;
        }
        return false; // 如果商品不存在，返回 false
    }

    // 結帳並刪除已勾選的商品
    public void checkout(List<Long> cartIds) {
        // 根據 cartIds 刪除已勾選的商品
        cartRepository.deleteByCartIdIn(cartIds); // 刪除指定 cartIds 的商品
    }

    // 根據 cartId 查找購物車商品 (新增此方法)
    public Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null); // 根據 cartId 查找購物車商品
    }

    // 更新購物車資料 (新增此方法)
    public void updateCart(Cart cart) {
        cartRepository.save(cart); // 更新購物車資料
    }

    // Add item to cart using CartRequest and return a CartResponse
    public CartResponse addCartItem(CartRequest request) {
        // 通过会员 ID 查找会员
        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
        if (!optionalMember.isPresent()) {
            return new CartResponse(false, "Member not found.");
        }

        Member member = optionalMember.get();

        // 查找商品的 salePrice 和商品名稱，通过 productId 查找商品
        Double salePrice = getProductSalePrice(request.getProductId());
        String productName = getProductName(request.getProductId());
        if (salePrice == null || productName == null) {
            return new CartResponse(false, "Product not found.");
        }

        // 新建购物车对象并设置相应的字段
        Cart cart = new Cart();
        cart.setMember(member); // 设置会员
        cart.setProductId(request.getProductId()); // 设置商品 ID
        cart.setQuantity(request.getQuantity()); // 设置商品数量
        cart.setLastUpdatedDate(LocalDateTime.now()); // 设置更新时间
        cart.setProductName(productName); // 设置商品名称

        // 设置商品价格
        cart.setSalePrice(salePrice); // 使用查询到的商品价格

        // 保存购物车项目到数据库
        cartRepository.save(cart);

        // 返回响应
        return new CartResponse(true, "Item added to cart successfully.");
    }

    // 获取商品的销售价格
    private Double getProductSalePrice(Integer productId) {
        // 根据商品ID查询商品的价格
        Optional<Product> product = productRepository.findById(productId);
        return product.map(p -> p.getSalePrice().doubleValue()).orElse(null);
    }

    // 获取商品的名称
    private String getProductName(Integer productId) {
        // 根据商品ID查询商品的名称
        Optional<Product> product = productRepository.findById(productId);
        return product.map(Product::getProductName).orElse(null); // 返回商品名称
    }

    // Update cart item (e.g., updating quantity)
    public CartResponse updateCart(CartRequest request) {
        // Fetch member using memberId
        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());

        if (!optionalMember.isPresent()) {
            return new CartResponse(false, "Member not found.");
        }

        Member member = optionalMember.get();

        // Find the cart item by member and product
        Cart cart = cartRepository.findByMemberAndProductId(member, request.getProductId());

        if (cart == null) {
            return new CartResponse(false, "Item not found in cart.");
        }

        // Update the quantity
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        // Prepare response
        return new CartResponse(true, "Cart updated successfully.");
    }
}
