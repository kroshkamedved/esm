package com.epam.esm.exception.handling;


import com.epam.esm.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatusException(ResponseStatusException e) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(e, e.getStatusCode().toString());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(exception, exception.getError());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(EntityCannotBeSaved.class)
    public ResponseEntity<ErrorDetails> handleEntityCannotBeSavedException(EntityCannotBeSaved exception) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(exception, HttpStatus.CONFLICT.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(EmptySetException.class)
    public ResponseEntity<ErrorDetails> handleEmptySetException(EmptySetException exception) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(exception, HttpStatus.NOT_FOUND.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleParsingRequestException(HttpMessageNotReadableException e) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(e, HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleParsingRequestException(DataIntegrityViolationException e) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(e, HttpStatus.BAD_REQUEST.toString());
        errorDetails.setErrorMessage("Data integrity violation");
        errorDetails.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(EntityUpdateException.class)
    public ResponseEntity<ErrorDetails> handleEntityUpdateException(EntityUpdateException e) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(e, HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(IrrelevantRequestParameterException.class)
    public ResponseEntity<ErrorDetails> handleIrrelevantRequestParameterException(IrrelevantRequestParameterException e) {
        ErrorDetails errorDetails = ErrorDetails.detailsOf(e, HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.badRequest().body(errorDetails);
    }
}
