package org.myprojects.simple_shop_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.mapper.CartMapper;
import org.myprojects.simple_shop_app.model.Cart;
import org.myprojects.simple_shop_app.model.dto.CartDTO;
import org.myprojects.simple_shop_app.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts") // PUT http://localhost:8080/products
@Slf4j
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(
            @RequestParam Long customerId
    ) {
        Cart cart = cartService.getCartByCustomerId(customerId);

        return ResponseEntity.ok(CartMapper.INSTANCE.cartToCartDTO(cart));
    }

    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        Cart cart = cartService.addProductToCart(cartId, productId, quantity);

        return ResponseEntity.ok(CartMapper.INSTANCE.cartToCartDTO(cart));
    }

    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ) {
        cartService.removeProductFromCart(cartId, productId);

        return ResponseEntity.ok("Продукт успешно удален из корзины!");
    }

    @PostMapping("/{cartId}/clear")
    public ResponseEntity<String> clearCart(
            @PathVariable Long cartId
    ) {
        cartService.clearCart(cartId);

        return ResponseEntity.ok("Корзина успешно очищена!");
    }
}
