package org.myprojects.simple_shop_app.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.ItemNotFoundException;
import org.myprojects.simple_shop_app.product.model.Product;
import org.myprojects.simple_shop_app.product.repository.ProductRepository;
import org.myprojects.simple_shop_app.product.service.ProductService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        // Some business logic
        try {
            log.info("[ProductServiceImpl][createProduct] стартовал: {}", JsonConverter.toJson(product).orElse(""));
            var response = productRepository.save(product);
            log.info("[ProductServiceImpl][createProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[ProductServiceImpl][createProduct] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при создании продукта!");
        }
    }

    @Override
    public Product updateProduct(Product product) {
        try {
            log.info("[ProductServiceImpl][updateProduct] стартовал: {}", JsonConverter.toJson(product).orElse(""));
            var response = productRepository.save(product);
            log.info("[ProductServiceImpl][updateProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[ProductServiceImpl][updateProduct] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении продукта!");
        }
    }

    @Override
    public Product updateProductPrice(Long productId, BigDecimal newPrice) {
//        Optional<Product> productOpt = productRepository.findById(productId);
//        if (productOpt.isEmpty()) {
//            throw new ProductNotFoundException("Requested product not found!");
//        }
//
//        return productRepository.save(
//                productOpt.get().toBuilder()
//                        .price(newPrice)
//                        .build()
//        );
        try {
            log.info("[ProductServiceImpl][updateProductPrice][productId={}] стартовал с newPrice={}", productId, newPrice);
            var response = productRepository.findById(productId)
                    .map(product -> productRepository.save(
                            product.toBuilder()
                                    .price(newPrice)
                                    .build()
                    ))
                    .orElseThrow(
                            () -> new ItemNotFoundException("Продукт не найден!")
                    );
            log.info("[ProductServiceImpl][updateProductPrice][productId={}] успешно отработал: {}", productId, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[ProductServiceImpl][updateProductPrice][productId={}] получена ошибка: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении цены продукта!");
        }
    }

    @Override
    public Product getProduct(Long productId) {
        try {
            log.info("[ProductServiceImpl][getProduct][productId={}] стратовал", productId);
            var response = productRepository.findById(productId)
                    .orElseThrow(() -> new ItemNotFoundException("Продукт не найден!"));

            log.info("[ProductServiceImpl][getProduct][productId={}] успешно отработал: {}",
                    productId, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[ProductServiceImpl][getProduct][productId={}] получена ошибка: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении деталей продукта!");
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        try {
            log.info("[ProductServiceImpl][deleteProduct][productId={}] стратовал", productId);
            productRepository.deleteById(productId);

            log.info("[ProductServiceImpl][deleteProduct][productId={}] успешно отработал",
                    productId);
        } catch (Exception e) {
            log.error("[ProductServiceImpl][deleteProduct][productId={}] получена ошибка: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении продукта!");
        }
    }

    @Override
    public List<Product> getProducts(Optional<String> productName, Optional<String> productCategory) {
        try {
            log.info("[ProductServiceImpl][getProducts] стратовал с productName={}, productCategory={}",
                    productName, productCategory);
            List<Product> response = new ArrayList<>();

            if (productName.isPresent() && productCategory.isPresent())
                response = productRepository.findByCategoryAndName(productCategory.get(), productName.get());
            else if (productName.isPresent())
                response = productRepository.findByName(productName.get());
            else if (productCategory.isPresent())
                response = productRepository.findByCategory(productCategory.get());
            else
                response = productRepository.findAll();

            log.info("[ProductServiceImpl][getProducts] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[ProductServiceImpl][getProducts] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка продуктов!");
        }
    }
}
