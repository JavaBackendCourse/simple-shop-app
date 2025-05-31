package org.myprojects.simple_shop_app.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.AppException;
import org.myprojects.simple_shop_app.product.mapper.AutoProductMapper;
import org.myprojects.simple_shop_app.product.model.dto.ProductDTO;
import org.myprojects.simple_shop_app.product.model.request.UpdateProductPriceRequest;
import org.myprojects.simple_shop_app.product.service.ProductService;
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

    @Operation(summary = "Получение продукта по ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Продукт успешно получен по ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Продукт не найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            )
    })
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

    @Operation(summary = "Получение списка продуктов")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Список продуктов успешно получен",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            )
    })
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

    @Operation(summary = "Создание продукта")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Продукт успешно создан",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            )
    })
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

    @Operation(summary = "Обновление продукта")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Продукт успешно обновлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Продукт не найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            )
    })
    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO product) {
        log.info("[ProductController][updateProduct] стратовал: {}", JsonConverter.toJson(product).orElse(""));
        var response = productService.updateProduct(
                AutoProductMapper.INSTANCE.productDTOToProduct(product)
        );
        log.info("[ProductController][updateProduct] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(AutoProductMapper.INSTANCE.productToProductDTO(response));
    }

    @Operation(summary = "Обновление цены продукта")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Цена продукта успешно обновлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Продукт не найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            )
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductPrice(
            @PathVariable Long productId, @RequestBody UpdateProductPriceRequest request) {
        log.info("[ProductController][updateProductPrice][productId={}] стратовал: {}", productId, JsonConverter.toJson(request).orElse(""));
        var response = productService.updateProductPrice(productId, request.newPrice());
        log.info("[ProductController][updateProductPrice][productId={}] получен успешный ответ: {}",
                productId, JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(AutoProductMapper.INSTANCE.productToProductDTO(response));
    }

    @Operation(summary = "Удаление продукта")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Продукт успешно удален",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Продукт не найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppException.class))
            )
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        log.info("[ProductController][deleteProduct][productId={}] стратовал", productId);
        productService.deleteProduct(productId);
        log.info("[ProductController][deleteProduct][productId={}] успешно отработал", productId);

        return ResponseEntity.ok("Продукт успешно удален!");
    }
}
