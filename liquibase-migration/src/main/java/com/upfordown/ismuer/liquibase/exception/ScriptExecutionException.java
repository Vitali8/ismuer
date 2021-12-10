package com.upfordown.ismuer.liquibase.exception;

public class ScriptExecutionException extends RuntimeException {

    public ScriptExecutionException() {
    }

    public ScriptExecutionException(final String message) {
        super(message);
    }

    public ScriptExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
