package com.errol.db2spring.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * Base exception for all db2spring errors.
 * Supports error codes and nested causes for better debugging.
 */
@Getter
public class Db2springException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Optional error code to categorize the exception
     * -- GETTER --
     * Getter for error code
     */
    private final String errorCode;

    /** Constructor with only message */
    public Db2springException(String message) {
        super(message);
        this.errorCode = null;
    }

    /** Constructor with message and cause */
    public Db2springException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /** Constructor with message and error code */
    public Db2springException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /** Constructor with message, cause, and error code */
    public Db2springException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "Db2springException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", cause=" + getCause() +
                '}';
    }
}