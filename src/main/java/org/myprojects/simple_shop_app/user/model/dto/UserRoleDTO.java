package org.myprojects.simple_shop_app.user.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleDTO {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    MANAGER("MANAGER"),
    SELLER("SELLER");

    private final String value;
}
