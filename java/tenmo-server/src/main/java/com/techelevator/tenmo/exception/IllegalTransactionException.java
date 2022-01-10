package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transaction not allowed")
public class IllegalTransactionException extends Exception {

    public IllegalTransactionException() {
        super("Transactions between the same account are not permitted");
    }
}