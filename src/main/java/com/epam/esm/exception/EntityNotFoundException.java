package com.epam.esm.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private Error error;

    public EntityNotFoundException(String message, Error error) {
        super(message);
        this.error = error;
    }
}
