package com.upfordown.ismuer.liquibase.exception;

public class NotSupportedFileException extends RuntimeException {

    public NotSupportedFileException() {
    }

    public NotSupportedFileException(final String message) {
        super(message);
    }

    public NotSupportedFileException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
