package org.myprojects.simple_shop_app.auth.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RefreshTokenRequest(
        @NotNull(message = "Refresh token is required")
        String refreshToken
) {
}
