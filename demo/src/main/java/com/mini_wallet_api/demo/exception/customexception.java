package com.mini_wallet_api.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class customexception extends RuntimeException {

    private final HttpStatus status;

    public customexception(String message,
                           HttpStatus status) {

        super(message);
        this.status = status;
    }


}