package com.fintech.database.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String object, Long id) {
        super(object + " with id " + id + " not found");
    }

    public ObjectNotFoundException(String object, String name) {
        super(object + " with name " + name + " not found");
    }
}
