package org.myprojects.simple_shop_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<AppException> productNotFound(ProductNotFoundException pNotFound) {
        AppException appException = AppException.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(pNotFound.getMessage())
                .build();

        return new ResponseEntity<>(appException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<AppException> runtimeException(RuntimeException runtimeException) {
        AppException appException = AppException.builder()
                .message(runtimeException.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(appException, HttpStatus.NOT_FOUND);
    }

}
