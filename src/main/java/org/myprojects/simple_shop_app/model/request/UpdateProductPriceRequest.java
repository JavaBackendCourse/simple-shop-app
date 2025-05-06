package org.myprojects.simple_shop_app.model.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateProductPriceRequest(
        BigDecimal newPrice
) {
}
