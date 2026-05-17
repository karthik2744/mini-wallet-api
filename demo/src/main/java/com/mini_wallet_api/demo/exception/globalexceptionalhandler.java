package com.mini_wallet_api.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class globalexceptionalhandler {

    @ExceptionHandler(customexception.class)
    public ResponseEntity<Map<String, Object>>
    handleCustomException(customexception ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status",
                ex.getStatus().value());

        response.put("message",
                ex.getMessage());

        return new ResponseEntity<>(
                response,
                ex.getStatus()
        );
    }


    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status", 400);

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
}