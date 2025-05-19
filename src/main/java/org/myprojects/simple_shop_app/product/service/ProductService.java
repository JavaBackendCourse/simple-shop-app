package org.myprojects.simple_shop_app.product.service;

import org.myprojects.simple_shop_app.product.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);

    Product updateProduct(Product product);

    Product updateProductPrice(Long productId, BigDecimal newPrice);

    Product getProduct(Long productId);

    void deleteProduct(Long productId);

    List<Product> getProducts(Optional<String> productName, Optional<String> productCategory);
}
