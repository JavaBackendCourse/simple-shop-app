package org.myprojects.simple_shop_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.mapper.AutoProductMapper;
import org.myprojects.simple_shop_app.mapper.ProductMapper;
import org.myprojects.simple_shop_app.model.Product;
import org.myprojects.simple_shop_app.model.dto.ProductDTO;
import org.myprojects.simple_shop_app.model.request.UpdateProductPriceRequest;
import org.myprojects.simple_shop_app.service.ProductService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products") // PUT http://localhost:8080/products
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductDetails(@PathVariable Long productId) {
        log.info("[ProductController][getProductDetails][productId={}] стартовал", productId);
        var response = productService.getProduct(productId);
        log.info("[ProductController][getProductDetails][productId={}] успешно отработал: {}",
                productId, JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(
                AutoProductMapper.INSTANCE.productToProductDTO(response)
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productCategory
    ) {
        log.info("[ProductController][getProducts] стартовал с productName={}, productCategory={}",
                productName, productCategory);
        var response = productService.getProducts(
                Optional.ofNullable(productName), Optional.ofNullable(productCategory)
        );
        log.info("[ProductController][getProducts] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(AutoProductMapper.INSTANCE.productsToDTOs(response));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        log.info("[ProductController][createProduct] стратовал: {}", JsonConverter.toJson(product).orElse(""));
        var response = productService.createProduct(
                AutoProductMapper.INSTANCE.productDTOToProduct(product)
        );
        log.info("[ProductController][createProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AutoProductMapper.INSTANCE.productToProductDTO(response));
    }

    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO product) {
        log.info("[ProductController][updateProduct] стратовал: {}", JsonConverter.toJson(product).orElse(""));
        var response = productService.updateProduct(
                AutoProductMapper.INSTANCE.productDTOToProduct(product)
        );
        log.info("[ProductController][updateProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(AutoProductMapper.INSTANCE.productToProductDTO(response));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductPrice(
            @PathVariable Long productId, @RequestBody UpdateProductPriceRequest request) {
        log.info("[ProductController][updateProductPrice][productId={}] стратовал: {}", productId, JsonConverter.toJson(request).orElse(""));
        var response = productService.updateProductPrice(productId, request.newPrice());
        log.info("[ProductController][updateProductPrice][productId={}] получен успешный ответ: {}",
                productId, JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(AutoProductMapper.INSTANCE.productToProductDTO(response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        log.info("[ProductController][deleteProduct][productId={}] стратовал", productId);
        productService.deleteProduct(productId);
        log.info("[ProductController][deleteProduct][productId={}] успешно отработал", productId);

        return ResponseEntity.ok("Продукт успешно удален!");
    }
}
