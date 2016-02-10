package com.bitcup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author bitcup
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Missing or unknown user")
public class ApiAuthException extends RuntimeException {
    public ApiAuthException(String message) {
        super(message);
    }
}
