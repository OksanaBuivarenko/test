package com.fintech.database.exception;

public class RelatedEntityNotFound extends RuntimeException {
    public RelatedEntityNotFound(String message) {
        super(message);
    }
}
