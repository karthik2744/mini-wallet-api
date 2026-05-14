package com.mini_wallet_api.demo.exception;

import org.springframework.http.HttpStatus;

public class customexception extends RuntimeException {

    private final HttpStatus status;

    public customexception(String message,
                           HttpStatus status) {

        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}