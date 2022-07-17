package com.debijenkorf.handler;

import com.debijenkorf.exception.ApplicationError;
import com.debijenkorf.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public final ResponseEntity<ApplicationError> handleApplicationException(final ApplicationException exception) {
        ApplicationError error = ApplicationError
                .builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
