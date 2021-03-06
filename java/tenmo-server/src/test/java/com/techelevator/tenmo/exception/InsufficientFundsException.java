package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Insufficient funds")
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
        super("Insufficient Funds to complete transfer");
    }
}