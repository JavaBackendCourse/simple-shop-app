package org.myprojects.simple_shop_app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.ItemNotFoundException;
import org.myprojects.simple_shop_app.model.Cart;
import org.myprojects.simple_shop_app.model.Customer;
import org.myprojects.simple_shop_app.repository.CartRepository;
import org.myprojects.simple_shop_app.repository.CustomerRepository;
import org.myprojects.simple_shop_app.repository.ProductRepository;
import org.myprojects.simple_shop_app.service.CartService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
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
            Customer customer = customerRepository.findById(customerId)
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
            throw new RuntimeException("Ошибка при создании корзины для покупателя!");
        }
    }

    @Override
    public Cart addProductToCart(Long cartId, Long productId, Integer quantity) {
        return null;
    }

    @Override
    public void removeProductFromCart(Long cartId, Long productId) {

    }

    @Override
    public void clearCart(Long cartId) {

    }
}
