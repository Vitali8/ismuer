package com.upfordown.ismuer.liquibase.exception;

public class ScriptConversionException extends RuntimeException {

    public ScriptConversionException() {
    }

    public ScriptConversionException(final String message) {
        super(message);
    }

    public ScriptConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
