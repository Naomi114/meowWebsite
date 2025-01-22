package tw.com.ispan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.com.ispan.domain.shop.CartItem;
import tw.com.ispan.dto.CartRequest;
import tw.com.ispan.dto.CartResponse;
import tw.com.ispan.service.shop.CartService;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pages/cart")
@CrossOrigin
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    // Show all items in the cart for the specified member
    @GetMapping("/list/{memberId}")
    public List<CartItem> getCartByMemberId(@PathVariable Integer memberId) {
        return cartService.getCartItemsByMemberId(memberId);
    }

    // Get the cart ID for the specified member
    @GetMapping("/{memberId}")
    public ResponseEntity<Integer> getCartIdByMemberId(@PathVariable Integer memberId) {
        Integer cartId = cartService.getCartByMemberId(memberId).getCartId();
        if (cartId != null) {
            return ResponseEntity.ok(cartId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Create a new cart and return the cart ID
    @PostMapping("/create")
    public ResponseEntity<Integer> createCart(@RequestBody Integer memberId) {
        try {
            Integer cartId = cartService.createCart(memberId).getCartId();
            return ResponseEntity.status(HttpStatus.CREATED).body(cartId);
        } catch (Exception e) {
            log.error("Error creating cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Add an item to the cart
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addCart(@RequestBody @Valid CartRequest request) {
        try {
            CartResponse response = cartService.addCartItem(request);
            return response.isSuccess() ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Error adding cart item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CartResponse(false, "Failed to add item to cart"));
        }
    }

    // Update the quantity of an item in the cart
    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestBody CartItem cartItem) {
        boolean updated = cartService.updateCartItemQuantity(cartItem.getCartItemId(), cartItem.getQuantity());
        return updated ? ResponseEntity.ok("Cart item updated successfully.")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to update cart item.");
    }

    // Delete a specified item from the cart
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Integer cartItemId) {
        boolean removed = cartService.removeCartItem(cartItemId);
        return removed ? ResponseEntity.ok("Item removed from cart.")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to remove item.");
    }

    // Clear the entire cart for the specified member
    @DeleteMapping("/clear/{memberId}")
    public ResponseEntity<String> clearCart(@PathVariable Integer memberId) {
        cartService.clearCartByMemberId(memberId);
        return ResponseEntity.ok("Cart cleared successfully.");
    }

    // Update the cart using a CartRequest
    @PostMapping("/updateCartRequest")
    public ResponseEntity<CartResponse> updateCart(@RequestBody @Valid CartRequest request) {
        CartResponse response = cartService.updateCart(request);
        return response.isSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
