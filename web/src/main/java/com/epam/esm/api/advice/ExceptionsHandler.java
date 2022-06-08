package com.epam.esm.api.advice;

import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException e) {
        String errorMessage = e.getErrorMessage();
        ErrorResponse errorResponse =
                new ErrorResponse(40401, errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateEntityException e) {
        ErrorResponse errorResponse =
                new ErrorResponse(40901, "Requested resource already exists");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed() {
        return new ResponseEntity<>(
                new ErrorResponse(405, "Method not found"),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotFound() {
        return new ResponseEntity<>(
                new ErrorResponse(404, "Method not found"),
                HttpStatus.NOT_FOUND);
    }
}
