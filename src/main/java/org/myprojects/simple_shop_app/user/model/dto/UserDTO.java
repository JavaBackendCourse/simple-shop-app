package org.myprojects.simple_shop_app.user.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String middleName,
        String phoneNumber,
        LocalDate birthDate,
        String email,
        String password,
        UserRoleDTO role,
        UserStatusDTO status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
