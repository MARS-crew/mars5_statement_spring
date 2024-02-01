package com.mars.statement.global.exception;

public class NotFoundException extends Exception {
    public NotFoundException(int value, String message) {
        super(message);
    }
}