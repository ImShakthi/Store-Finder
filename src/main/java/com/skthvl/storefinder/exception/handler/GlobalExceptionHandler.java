package com.skthvl.storefinder.exception.handler;

import com.skthvl.storefinder.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgument(final IllegalArgumentException ex) {
    return new ErrorResponse(ex.getMessage());
  }

  /**
   * Handles {@link DataIntegrityViolationException} by returning a BAD_REQUEST response with the
   * exception message.
   *
   * @param ex the DataIntegrityViolationException that was thrown
   * @return ResponseEntity containing the error message with HTTP status 400 (BAD_REQUEST)
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
      final DataIntegrityViolationException ex) {

    final String fullMessage =
        ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
    final String constraintMessage = extractConstraintViolationMessage(fullMessage);

    final String message =
        (constraintMessage != null) ? constraintMessage : "Conflict: Record already exists.";

    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(message));
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
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              errors.put(error.getField(), error.getDefaultMessage());
            });
    return errors;
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
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String field = violation.getPropertyPath().toString();
              String message = violation.getMessage();
              errors.put(field, message);
            });
    return errors;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGeneric(final Exception ex) {
    ex.printStackTrace();
    return new ErrorResponse("something went wrong, please try again later");
  }

  private String extractConstraintViolationMessage(final String fullMessage) {
    if (fullMessage == null) {
      return null;
    }

    final Pattern pattern = Pattern.compile("ERROR:\\s*(.+?)\\n");
    final Matcher matcher = pattern.matcher(fullMessage);

    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    return null;
  }
}
