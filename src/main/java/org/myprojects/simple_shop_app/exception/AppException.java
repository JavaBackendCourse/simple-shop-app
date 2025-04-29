package org.myprojects.simple_shop_app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AppException {
    private final Integer statusCode;
    private final String message;
}
