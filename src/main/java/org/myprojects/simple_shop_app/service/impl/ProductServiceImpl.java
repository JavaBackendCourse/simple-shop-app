package org.myprojects.simple_shop_app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.ProductNotFoundException;
import org.myprojects.simple_shop_app.model.Product;
import org.myprojects.simple_shop_app.repository.ProductRepository;
import org.myprojects.simple_shop_app.service.ProductService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.stereotype.Service;

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
        log.info("[ProductServiceImpl][createProduct] стартовал: {}", JsonConverter.toJson(product).orElse(""));
        var response = productRepository.save(product);
        log.info("[ProductServiceImpl][createProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return response;
    }

    @Override
    public Product updateProduct(Product product) {
        log.info("[ProductServiceImpl][updateProduct] стартовал: {}", JsonConverter.toJson(product).orElse(""));
        var response = productRepository.save(product);
        log.info("[ProductServiceImpl][updateProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return response;
    }

    @Override
    public Product updateProductPrice(Long productId, Double newPrice) {
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
        log.info("[ProductServiceImpl][updateProduct][productId={}] стартовал с newPrice={}", productId, newPrice);
        var response = productRepository.findById(productId)
                .map(product -> productRepository.save(
                        product.toBuilder()
                                .price(newPrice)
                                .build()
                ))
                .orElseThrow(
                        () -> new ProductNotFoundException("Requested product not found!")
                );
        log.info("[ProductServiceImpl][updateProduct][productId={}] успешно отработал: {}",  productId, JsonConverter.toJson(response).orElse(""));

        return response;
    }

    @Override
    public Product getProduct(Long productId) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public List<Product> getProducts(Optional<String> productName, Optional<String> productCategory) {
        return List.of();
    }
}
