package org.myprojects.simple_shop_app.model.request;

import lombok.Builder;

@Builder
public record UpdateCustomerEmailRequest(
        String newEmail
) {
}

