package com.skthvl.storefinder.exception.handler;

import com.skthvl.storefinder.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles {@link IllegalArgumentException} by returning a BAD_REQUEST response with the exception
   * message.
   *
   * @param ex the IllegalArgumentException that was thrown
   * @return ResponseEntity containing the error message with HTTP status 400 (BAD_REQUEST)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(final IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles validation errors that occur during request processing. Maps field-specific validation
   * errors to their corresponding error messages.
   *
   * @param ex the MethodArgumentNotValidException containing validation errors
   * @return ResponseEntity containing a map of field names to error messages with HTTP status 400
   *     (BAD_REQUEST)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              errors.put(error.getField(), error.getDefaultMessage());
            });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  /**
   * Handles constraint violation exceptions that occur during validation. Maps violated constraints
   * to their corresponding error messages.
   *
   * @param ex the ConstraintViolationException containing validation errors
   * @return ResponseEntity containing a map of constraint paths to error messages with HTTP status
   *     400 (BAD_REQUEST)
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String field = violation.getPropertyPath().toString();
              String message = violation.getMessage();
              errors.put(field, message);
            });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(final Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("something went wrong, please try again later"));
  }
}
