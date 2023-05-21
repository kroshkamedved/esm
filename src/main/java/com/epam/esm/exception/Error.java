package com.epam.esm.exception;

import lombok.Getter;

@Getter
public enum Error {
    GiftCertificateNotFound(40401, "GiftCertificateNotFound"), TagNotFound(40402, "TagNotFound");

    private final int code;
    private final String description;

    private Error(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
