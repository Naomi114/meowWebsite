package tw.com.ispan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.com.ispan.dto.CartRequest;
import tw.com.ispan.dto.CartResponse;
import tw.com.ispan.service.shop.CartService;
import tw.com.ispan.domain.shop.Cart;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.validation.Valid;

@RestController // Use @RestController for RESTful API responses
@RequestMapping("/pages/cart") // Modify the path to avoid conflict with other controllers
@CrossOrigin // Enable Cross-Origin Request Sharing
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class); // Initialize the logger

    @Autowired
    private CartService cartService;

    // Show all items in the cart for a specific member
    @GetMapping("/list/{memberId}")
    public List<Cart> getCartByMemberId(@PathVariable Integer memberId) {
        return cartService.getCartByMemberId(memberId);
    }

    // Add item to the cart using CartRequest and return a CartResponse
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addCartItem(@RequestBody @Valid CartRequest request) {
        try {
            // Perform validation or processing logic
            CartResponse response = cartService.addCartItem(request);
            return response.isSuccess() ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // Log error and return internal server error response
            log.error("Error adding cart item", e); // Now using the logger properly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CartResponse(false, "Failed to add item to cart"));
        }
    }

    // Update quantity of an item in the cart
    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestBody Cart cart) {
        boolean updated = cartService.updateQuantity(cart.getCartId(), cart.getQuantity());
        if (updated) {
            return ResponseEntity.ok("Cart updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to update cart.");
        }
    }

    // Remove item from the cart
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeItem(@PathVariable Long cartId) {
        boolean removed = cartService.removeItem(cartId);
        if (removed) {
            return ResponseEntity.ok("Item removed from cart.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to remove item.");
        }
    }

    // Update cart using CartRequest and return a CartResponse
    @PostMapping("/updateCartRequest")
    public ResponseEntity<CartResponse> updateCart(@RequestBody @Valid CartRequest request) {
        CartResponse response = cartService.updateCart(request);
        return response.isSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
