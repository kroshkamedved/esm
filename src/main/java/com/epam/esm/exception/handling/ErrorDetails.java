package com.epam.esm.exception.handling;

import com.epam.esm.exception.Error;
import lombok.Data;

@Data
public class ErrorDetails {
    private String errorMessage;
    private String errorCode;

    public static ErrorDetails detailsOf(Exception e, Error errorCode) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorMessage(e.getMessage());
        errorDetails.setErrorCode(String.valueOf(errorCode.getCode()));
        return errorDetails;
    }

    public static ErrorDetails detailsOf(Exception e, String errorCode) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorMessage(e.getMessage());
        errorDetails.setErrorCode(errorCode);
        return errorDetails;
    }
}
