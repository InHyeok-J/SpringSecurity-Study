package com.security.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> Handler(CustomException e) {
        log.error("ERROR !!" + e.getMessage());
        return ResponseEntity
            .status(400)
            .body(new ErrorDTO(e.getMessage(), 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){

        log.error("ERROR !!" + e.getMessage());
        return ResponseEntity
            .status(500)
            .body(new ErrorDTO(e.getMessage(), 500));
    }



}
