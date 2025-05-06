package org.myprojects.simple_shop_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.myprojects.simple_shop_app.model.Cart;
import org.myprojects.simple_shop_app.model.CartItem;
import org.myprojects.simple_shop_app.model.dto.CartDTO;
import org.myprojects.simple_shop_app.model.dto.CartItemDTO;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "items", target = "items")
    CartDTO cartToCartDTO(Cart cart);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "items", target = "items")
    Cart cartDTOToCart(CartDTO cartDTO);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    CartItemDTO cartItemToCartItemDTO(CartItem cartItem);

    @Mapping(source = "productId", target = "product.id")
    CartItem cartItemDTOToCartItem(CartItemDTO cartItemDTO);
}
