package com.epam.esm.exception.handling;

import lombok.Data;

@Data
public class ErrorDetails {
    private String errorMessage;
    private String errorCode;

    public static ErrorDetails detailsOf(Exception e, String errorCode) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorMessage(e.getMessage());
        errorDetails.setErrorCode(errorCode);
        return errorDetails;
    }
}
