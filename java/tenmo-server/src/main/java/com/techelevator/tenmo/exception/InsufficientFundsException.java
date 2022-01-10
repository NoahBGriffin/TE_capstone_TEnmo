package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient funds")
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
        super("Insufficient Funds to complete transfer");
    }
}