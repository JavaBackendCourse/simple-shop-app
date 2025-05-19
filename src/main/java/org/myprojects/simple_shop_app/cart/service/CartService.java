package org.myprojects.simple_shop_app.cart.service;

import org.myprojects.simple_shop_app.cart.model.Cart;

public interface CartService {
    Cart getCartByCustomerId(Long customerId);

    Cart addProductToCart(Long cartId, Long productId, Integer quantity);

    void removeProductFromCart(Long cartId, Long productId);

    void clearCart(Long cartId);
}
