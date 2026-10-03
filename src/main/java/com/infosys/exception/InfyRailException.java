package com.infosys.exception;

/**
 * Business exception. The message is a key in ValidationMessages.properties
 * (e.g. "Service.ROUTEID_NOT_FOUND"), resolved by ExceptionControllerAdvice.
 */
public class InfyRailException extends Exception {

    private static final long serialVersionUID = 1L;

    public InfyRailException(String message) {
        super(message);
    }
}
