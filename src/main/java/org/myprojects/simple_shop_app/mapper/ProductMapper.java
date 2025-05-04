package org.myprojects.simple_shop_app.mapper;

import org.myprojects.simple_shop_app.model.Product;
import org.myprojects.simple_shop_app.model.dto.ProductDTO;

import java.util.List;

public class ProductMapper {
    public static List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(ProductMapper::productToProductDTO).toList();
    }

    public static ProductDTO productToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .productCategory(product.getCategory())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public static Product productDTOToProduct(ProductDTO product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getProductCategory())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
