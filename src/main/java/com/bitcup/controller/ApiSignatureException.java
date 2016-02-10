package com.bitcup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author bitcup
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request signature missing or mismatched")
public class ApiSignatureException extends RuntimeException {
    public ApiSignatureException(String message) {
        super(message);
    }
}
