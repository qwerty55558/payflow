package com.fds.payflow.exceptions;

public class OutOfBalanceException extends RuntimeException {
    public OutOfBalanceException(String message) {
        super("Out of Balance: " + message);
    }
}
