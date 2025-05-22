package org.myprojects.simple_shop_app.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record RegistrationRequest(
        @NotNull(message = "First name is required")
        String firstName,

        @NotNull(message = "Last name is required")
        String lastName,

        String middleName,

        @NotNull(message = "Birth date is required")
        LocalDate birthDate,

        @Email(message = "Invalid email format")
        @NotNull(message = "Email is required")
        String email,

        String phoneNumber,

        @NotNull(message = "Role is required")
        String role,

        @NotNull(message = "Password is required")
        String password
) {
}
