package com.epam.esm.exception;

import lombok.Getter;

@Getter
public enum Error {
    GiftCertificateNotFound(40401, "GiftCertificate Not Found"),
    TagNotFound(40402, "Tag Not Found"),
    UserNotFound(40403, "User Not Found"),
    OrderNotFound(40404, "Order Not Found"),
    IrrelevantParameters(4001, "Irrelevant Request Parameters");


    private final int code;
    private final String description;

    private Error(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
