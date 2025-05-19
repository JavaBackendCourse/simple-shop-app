package org.myprojects.simple_shop_app.user.model.request;

import lombok.Builder;
import org.myprojects.simple_shop_app.user.model.UserRole;
import org.myprojects.simple_shop_app.user.model.UserStatus;

import java.time.LocalDate;
import java.util.Optional;

@Builder(toBuilder = true)
public record UpdateUserRequest(
        Long userId,
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> middleName,
        Optional<LocalDate> birthDate,
        Optional<String> email,
        Optional<String> phoneNumber,
        Optional<UserRole> role,
        Optional<UserStatus> status,
        Optional<String> newEncodedPassword
) {
}
