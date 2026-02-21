package com.x.sentinel.exception;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException(String s) {
        super(s);
    }
}
