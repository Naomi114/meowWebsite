package tw.com.ispan.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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

    // Get cart items by member ID
    public List<CartItem> getCartItemsByMemberId(Integer memberId) {
        return cartItemRepository.findByCart_Member_Id(memberId);
    }

    // Get the cart by member ID
    public Cart getCartByMemberId(Integer memberId) {
        return cartRepository.findByMember_Id(memberId);
    }

    // Find a cart item by its ID
    public CartItem findCartItemById(Integer cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }

    @Transactional
    public CartResponse addCartItem(CartRequest request) {
        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
        if (optionalMember.isEmpty()) {
            return new CartResponse(false, "Member not found.");
        }
        Member member = optionalMember.get();

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return new CartResponse(false, "Product not found.");
        }

        Cart cart = cartRepository.findByMember_Id(member.getId());
        if (cart == null) {
            cart = createCart(member.getId());
        }

        CartItem existingItem = cartItemRepository.findByCart_Member_IdAndProduct_ProductId(member.getId(),
                request.getProductId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setLastUpdatedDate(LocalDateTime.now());
            cartItemRepository.save(existingItem);
            return new CartResponse(true, "Item quantity updated in cart.");
        }

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

    // Update the quantity of a cart item
    @Transactional
    public boolean updateCartItemQuantity(Integer cartItemId, int quantity) {
        return cartItemRepository.findById(cartItemId).map(cartItem -> {
            cartItem.setQuantity(quantity);
            cartItem.setLastUpdatedDate(LocalDateTime.now());
            cartItemRepository.save(cartItem);
            return true;
        }).orElse(false);
    }

    // Add a new updateCartItem method to match controller expectations
    public boolean updateCartItem(Integer cartItemId, Integer quantity) {
        return updateCartItemQuantity(cartItemId, quantity);
    }

    // Remove a cart item
    @Transactional
    public boolean removeCartItem(Integer cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
            return true;
        }
        return false;
    }

    // Clear all cart items for a member
    @Transactional
    public void clearCartByMemberId(Integer memberId) {
        List<CartItem> cartItems = cartItemRepository.findByCart_Member_Id(memberId);
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
        }
    }

    // Update the cart with a CartRequest
    @Transactional
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

    // Checkout and remove items from the cart
    @Transactional
    public void checkout(List<Integer> cartItemIds) {
        if (!CollectionUtils.isEmpty(cartItemIds)) {
            cartItemRepository.deleteAllByCartItemIdIn(cartItemIds);
        }
    }

    // Create a cart for a member
    @Transactional
    public Cart createCart(Integer memberId) {
        return memberRepository.findById(memberId).map(member -> {
            Cart cart = new Cart();
            cart.setMember(member);
            cart.setLastUpdatedDate(LocalDateTime.now());
            return cartRepository.save(cart);
        }).orElse(null);
    }

    public CartItem getCartItemById(Integer cartItemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartItemById'");
    }
}
