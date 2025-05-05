package org.myprojects.simple_shop_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.myprojects.simple_shop_app.model.Product;
import org.myprojects.simple_shop_app.model.dto.ProductDTO;

import java.util.List;

@Mapper
public interface AutoProductMapper {
    AutoProductMapper INSTANCE = Mappers.getMapper(AutoProductMapper.class);

    @Mapping(source = "category", target = "productCategory")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "category", target = "productCategory")
    List<ProductDTO> productsToDTOs(List<Product> products);

    @Mapping(source = "productCategory", target = "category")
    Product productDTOToProduct(ProductDTO productDTO);
}
