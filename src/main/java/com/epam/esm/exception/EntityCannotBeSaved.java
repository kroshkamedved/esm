package com.epam.esm.exception;

public class EntityCannotBeSaved extends RuntimeException {
    public EntityCannotBeSaved(String message) {
        super(message);
    }
}
