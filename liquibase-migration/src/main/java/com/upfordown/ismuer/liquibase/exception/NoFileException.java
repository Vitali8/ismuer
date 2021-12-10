package com.upfordown.ismuer.liquibase.exception;

public class NoFileException extends RuntimeException {

    public NoFileException() {
    }

    public NoFileException(final String message) {
        super(message);
    }

    public NoFileException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
