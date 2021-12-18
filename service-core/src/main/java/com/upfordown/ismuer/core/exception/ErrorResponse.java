package com.upfordown.ismuer.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String message = null;
    private List<ErrorMessage> errors = null;

    public ErrorResponse(String code) {
        this.code = code;
    }

    public ErrorResponse(String code, String message) {
        this(code);
        this.message = message;
    }

    public ErrorResponse(String code, List<ErrorMessage> errors) {
        this(code);
        this.errors = errors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorMessage> errors) {
        this.errors = errors;
    }
}
