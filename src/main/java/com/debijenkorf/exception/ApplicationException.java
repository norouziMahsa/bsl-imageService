package com.debijenkorf.exception;

public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Exception exception) {
        super(message, exception);
    }

    public ApplicationException(Exception exception) {
        super(exception);
    }
}
