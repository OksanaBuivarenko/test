package com.fintech.database.exception;

public class ObjectAlreadyExistsException extends Throwable {
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
