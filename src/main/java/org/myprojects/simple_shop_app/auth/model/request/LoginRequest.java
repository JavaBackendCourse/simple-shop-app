package org.myprojects.simple_shop_app.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginRequest(
        @Email(message = "Invalid email format")
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        String password
) {
}
