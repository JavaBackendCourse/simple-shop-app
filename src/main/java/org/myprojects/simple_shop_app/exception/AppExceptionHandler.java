package org.myprojects.simple_shop_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<AppException> customerNotFound(ItemNotFoundException cNotFound) {
        AppException appException = AppException.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(cNotFound.getMessage())
                .build();

        return new ResponseEntity<>(appException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<AppException> illegalArgException(IllegalArgumentException illegalArgException) {
        AppException appException = AppException.builder()
                .message(illegalArgException.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(appException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<AppException> runtimeException(RuntimeException runtimeException) {
        AppException appException = AppException.builder()
                .message(runtimeException.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(appException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
