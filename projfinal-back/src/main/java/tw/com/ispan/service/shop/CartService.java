package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.dto.CartRequest;
import tw.com.ispan.dto.CartResponse;
import tw.com.ispan.repository.shop.CartItemRepository;
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

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getCartItemsByMemberId(Integer memberId) {
        return cartItemRepository.findByCart_Member_Id(memberId);
    }

    public Cart getCartByMemberId(Integer memberId) {
        return cartRepository.findByMember_Id(memberId);
    }

    public CartItem findCartItemById(Integer cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }

    @Transactional
    public CartResponse addCartItem(CartRequest request) {
        // Check if the member exists
        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
        if (optionalMember.isEmpty()) {
            return new CartResponse(false, "Member not found.");
        }
        Member member = optionalMember.get();

        // Check if the product exists
        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return new CartResponse(false, "Product not found.");
        }

        // Retrieve or create a cart for the member
        Cart cart = cartRepository.findByMember_Id(member.getId());
        if (cart == null) {
            cart = createCart(member.getId());
        }

        // Check if the product is already in the cart
        CartItem existingItem = cartItemRepository.findByCart_Member_IdAndProduct_ProductId(member.getId(),
                request.getProductId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setLastUpdatedDate(LocalDateTime.now());
            cartItemRepository.save(existingItem);
            return new CartResponse(true, "Item quantity updated in cart.");
        }

        // Add new product to the cart
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setProductName(product.getProductName());
        cartItem.setSalePrice(product.getSalePrice().doubleValue());
        cartItem.setLastUpdatedDate(LocalDateTime.now());
        cartItemRepository.save(cartItem);

        return new CartResponse(true, "Item added to cart successfully.");
    }

    public boolean updateCartItemQuantity(Integer cartItemId, int quantity) {
        return cartItemRepository.findById(cartItemId).map(cartItem -> {
            cartItem.setQuantity(quantity);
            cartItem.setLastUpdatedDate(LocalDateTime.now());
            cartItemRepository.save(cartItem);
            return true;
        }).orElse(false);
    }

    public boolean removeCartItem(Integer cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
            return true;
        }
        return false;
    }

    public void clearCartByMemberId(Integer memberId) {
        List<CartItem> cartItems = cartItemRepository.findByCart_Member_Id(memberId);
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
        }
    }

    public CartResponse updateCart(CartRequest request) {
        return memberRepository.findById(request.getMemberId()).map(member -> {
            CartItem cartItem = cartItemRepository.findByCart_Member_IdAndProduct_ProductId(member.getId(),
                    request.getProductId());
            if (cartItem == null) {
                return new CartResponse(false, "Item not found in cart.");
            }
            cartItem.setQuantity(request.getQuantity());
            cartItem.setLastUpdatedDate(LocalDateTime.now());
            cartItemRepository.save(cartItem);
            return new CartResponse(true, "Cart updated successfully.");
        }).orElse(new CartResponse(false, "Member not found."));
    }

    public void checkout(List<Integer> cartItemIds) {
        if (cartItemIds != null && !cartItemIds.isEmpty()) {
            cartItemRepository.deleteAllByCartItemIdIn(cartItemIds);
        }
    }

    public Cart createCart(Integer memberId) {
        return memberRepository.findById(memberId).map(member -> {
            Cart cart = new Cart();
            cart.setMember(member);
            cart.setLastUpdatedDate(LocalDateTime.now());
            return cartRepository.save(cart);
        }).orElse(null);
    }
}
