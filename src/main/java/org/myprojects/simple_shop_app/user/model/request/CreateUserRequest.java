package org.myprojects.simple_shop_app.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.myprojects.simple_shop_app.user.model.UserRole;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record CreateUserRequest(
        @NotNull(message = "FirstName is required")
        String firstName,
        @NotNull(message = "LastName is required")
        String lastName,
        String middleName,

        @NotNull(message = "Date of birth is required")
        LocalDate birthDate,

        @Email(message = "Invalid email format")
        @NotNull(message = "Email is required")
        String email,

        String phoneNumber,

        @NotNull(message = "UserRole is required")
        UserRole role,

        @NotNull(message = "Password is required")
        String encodedPassword
) {
}
