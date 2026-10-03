package com.infosys.exception;

import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvice.class);

    @Autowired
    private Environment environment;

    // Business exceptions -> message key resolved from ValidationMessages.properties
    @ExceptionHandler(InfyRailException.class)
    public ResponseEntity<ErrorMessage> infyRailExceptionHandler(InfyRailException exception) {
        LOGGER.error(exception.getMessage());
        String message = environment.getProperty(exception.getMessage(), exception.getMessage());
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // @Valid @RequestBody DTO failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> dtoValidationHandler(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(", "));
        LOGGER.error(message);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // @PathVariable / @RequestParam / @MatrixVariable failures (class-level @Validated)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> pathValidationHandler(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .distinct()
                .collect(Collectors.joining(", "));
        LOGGER.error(message);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // Spring 6.1+ built-in method validation (in case @Validated is removed from a controller)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> methodValidationHandler(HandlerMethodValidationException exception) {
        String message = exception.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(", "));
        LOGGER.error(message);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // Non-numeric ids, malformed JSON, missing request/matrix parameters
    @ExceptionHandler({ TypeMismatchException.class, HttpMessageNotReadableException.class,
            ServletRequestBindingException.class })
    public ResponseEntity<ErrorMessage> badRequestHandler(Exception exception) {
        LOGGER.error(exception.getMessage());
        return build(HttpStatus.BAD_REQUEST, environment.getProperty("General.INVALID_REQUEST"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> methodNotAllowedHandler(Exception exception) {
        LOGGER.error(exception.getMessage());
        return build(HttpStatus.METHOD_NOT_ALLOWED, exception.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorMessage> mediaTypeHandler(Exception exception) {
        LOGGER.error(exception.getMessage());
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> generalExceptionHandler(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, environment.getProperty("General.EXCEPTION_MESSAGE"));
    }

    private ResponseEntity<ErrorMessage> build(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorMessage(status.value(), message), status);
    }
}
