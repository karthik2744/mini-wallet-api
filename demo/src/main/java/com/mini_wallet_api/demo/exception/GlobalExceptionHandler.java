package com.mini_wallet_api.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class GlobalExceptionHandler {

    // CUSTOM EXCEPTIONS
    @ExceptionHandler(CustomException.class)

    public ResponseEntity<Map<String, Object>>
    handleCustomException(CustomException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                ex.getStatus().value()
        );

        response.put(
                "message",
                ex.getMessage()
        );

        return new ResponseEntity<>(
                response,
                ex.getStatus()
        );
    }

    // VALIDATION EXCEPTIONS
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )

    public ResponseEntity<Map<String, Object>>
    handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        response.put(
                "message",

                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // GENERAL EXCEPTIONS
    @ExceptionHandler(Exception.class)

    public ResponseEntity<Map<String, Object>>
    handleGeneralException(Exception ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        response.put(
                "message",
                ex.getMessage()
        );

        return new ResponseEntity<>(

                response,

                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}