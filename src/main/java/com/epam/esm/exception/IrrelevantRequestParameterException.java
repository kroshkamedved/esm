package com.epam.esm.exception;

import lombok.Getter;

@Getter
public class IrrelevantRequestParameterException extends RuntimeException {
    private Error error;

    public IrrelevantRequestParameterException(String s, Error error) {
        super(s);
        this.error = error;
    }
}
