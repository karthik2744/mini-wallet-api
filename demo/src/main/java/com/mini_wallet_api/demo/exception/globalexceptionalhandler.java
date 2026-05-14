package com.mini_wallet_api.demo.exception;

import org.springframework.http.ResponseEntity;
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


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleException(Exception ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                500
        );

        response.put(
                "message",
                "Internal Server Error"
        );

        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}