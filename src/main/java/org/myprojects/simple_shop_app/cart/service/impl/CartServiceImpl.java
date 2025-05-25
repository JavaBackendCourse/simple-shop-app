package org.myprojects.simple_shop_app.cart.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.ItemNotFoundException;
import org.myprojects.simple_shop_app.cart.model.Cart;
import org.myprojects.simple_shop_app.cart.model.CartItem;
import org.myprojects.simple_shop_app.product.model.Product;
import org.myprojects.simple_shop_app.cart.repository.CartRepository;
import org.myprojects.simple_shop_app.product.repository.ProductRepository;
import org.myprojects.simple_shop_app.cart.service.CartService;
import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.UserRole;
import org.myprojects.simple_shop_app.user.repository.UserRepository;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart getCartByCustomerId(Long customerId) {
        try {
            log.info("[CartServiceImpl][getCartByCustomerId][customerId={}] стартовал", customerId);
            var response = cartRepository.findByCustomerId(customerId)
                    .orElseGet(() -> createCartForCustomer(customerId));

            log.info("[CartServiceImpl][getCartByCustomerId][customerId={}] получен успешный ответ: {}",
                    customerId, JsonConverter.toJson(response).orElse(""));
            return response;
        } catch (Exception e) {
            log.info("[CartServiceImpl][getCartByCustomerId][customerId={}] получена ошибка: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении корзины покупателя!");
        }
    }

    private Cart createCartForCustomer(Long customerId) {
        try {
            log.info("[CartServiceImpl][createCartForCustomer][customerId={}] стартовал", customerId);
            User customer = customerRepository.findByIdAndRole(customerId, UserRole.CUSTOMER)
                    .orElseThrow(
                            () -> new ItemNotFoundException("Покупатель не найден!")
                    );
            Cart cart = Cart.builder()
                    .customer(customer)
                    .items(new ArrayList<>())
                    .build();

            customer.setCart(cart);

            var response = customerRepository.save(customer).getCart();

            log.info("[CartServiceImpl][createCartForCustomer][customerId={}] получен успешный ответ: {}",
                    customerId, JsonConverter.toJson(response).orElse(""));
            return response;
        } catch (Exception e) {
            log.info("[CartServiceImpl][createCartForCustomer][customerId={}] получена ошибка: {}", customerId, e.getMessage(), e);
            if (e instanceof ItemNotFoundException notFound) {
                throw notFound;
            }
            throw new RuntimeException("Ошибка при создании корзины для покупателя!");
        }
    }

    @Override
    @Transactional
    public Cart addProductToCart(Long cartId, Long productId, Integer quantity) {
        try {
            log.info("[CartServiceImpl][addProductToCart][cartId={}] стартовал с productId={}", cartId, productId);

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(
                            () -> new ItemNotFoundException("Корзина не найдена!")
                    );
            Product product = productRepository.findById(productId)
                    .orElseThrow(
                            () -> new ItemNotFoundException("Продукт не найден!")
                    );

            if (product.getQuantity() == null)
                product.setQuantity(0);

            if (product.getQuantity() < quantity) {
                throw new RuntimeException(String.format("Продукта %s недостаточно на складе!", product.getName()));
            }

            Optional<CartItem> existingItemOptional = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            existingItemOptional.ifPresent(existingItem ->
                    existingItem.setQuantity(existingItem.getQuantity() + quantity)
            );

            if (existingItemOptional.isEmpty()) {
                CartItem newItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(quantity)
                        .build();

                cart.getItems().add(newItem);
            }

            cart.updateTotalPrice();

            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            var response = cartRepository.save(cart);

            log.info("[CartServiceImpl][addProductToCart][cartId={}] получен успешный ответ: {}", cartId, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.info("[CartServiceImpl][addProductToCart][cartId={}] получена ошибка: {}", cartId, e.getMessage(), e);
            if (e instanceof ItemNotFoundException notFound) {
                throw notFound;
            }
            throw new RuntimeException("Ошибка при добавлении продукта в корзину!");
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        try {
            log.info("[CartServiceImpl][removeProductFromCart][cartId={}] стратовал с productId = {}", cartId, productId);

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(
                            () -> new ItemNotFoundException("Корзина не найдена!")
                    );

            CartItem itemToRemove = cart.getItems().stream().filter(
                    item -> item.getProduct().getId().equals(productId)
            ).findFirst().orElseThrow(
                    () -> new ItemNotFoundException("Продукт не найден в корзине!")
            );

            cart.getItems().remove(itemToRemove);

            cart.updateTotalPrice();

            log.info("[CartServiceImpl][removeProductFromCart][cartId={}] успешно отработал!", cartId);
        } catch (Exception e) {
            log.info("[CartServiceImpl][removeProductFromCart][cartId={}] получена ошибка: {}", cartId, e.getMessage(), e);
            if (e instanceof ItemNotFoundException notFound) {
                throw notFound;
            }

            throw new RuntimeException("Ошибка при удалении продукта из корзины!");
        }
    }

    @Override
    public void clearCart(Long cartId) {
        try {
            log.info("[CartServiceImpl][clearCart][cartId={}] стратовал", cartId);
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(
                            () -> new ItemNotFoundException("Корзина не найдена!")
                    );

            cart.getItems().clear();
            cart.updateTotalPrice();
            cartRepository.save(cart);

            log.info("[CartServiceImpl][clearCart][cartId={}] успешно отработал!", cartId);
        } catch (Exception e) {
            log.info("[CartServiceImpl][clearCart][cartId={}] получена ошикба: {}", cartId, e.getMessage(), e);
            if (e instanceof ItemNotFoundException notFound) {
                throw notFound;
            }

            throw new RuntimeException("Ошбка при очистке корзины!");
        }
    }
}
