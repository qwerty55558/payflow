package com.fds.payflow.exceptions;

public class NullAccountException extends RuntimeException {
    public NullAccountException(String message) {
        super(message);
    }
}
