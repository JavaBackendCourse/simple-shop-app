package org.myprojects.simple_shop_app.auth.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginResponse(
        @NotNull
        String accessToken,
        @NotNull
        String refreshToken
) {
}
