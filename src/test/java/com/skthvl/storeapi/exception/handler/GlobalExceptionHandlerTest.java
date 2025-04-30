package com.skthvl.storeapi.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.skthvl.storeapi.exception.type.InvalidInputException;
import com.skthvl.storeapi.exception.type.StoreNotFoundException;
import com.skthvl.storeapi.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Slf4j
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  void shouldHandleIllegalArgumentException() {
    // Given
    final Exception ex = new IllegalArgumentException("Invalid input");

    // When
    final ErrorResponse response = handler.handleIllegalArgument(ex);

    // Then
    assertEquals(response.getMessage(), "Invalid input");
  }

  @Test
  void shouldHandleStoreNotFoundException() {
    // Given
    final Exception ex = new StoreNotFoundException("Store not found");

    // When
    final ErrorResponse response = handler.handleIllegalArgument(ex);

    // Then
    assertEquals(response.getMessage(), "Store not found");
  }

  @Test
  void shouldHandleInvalidInputException() {
    // Given
    final Exception ex = new InvalidInputException("Input is not valid");

    // When
    final ErrorResponse response = handler.handleIllegalArgument(ex);

    // Then
    assertEquals(response.getMessage(), "Input is not valid");
  }

  @Test
  void shouldHandleMissingServletRequestParameterException() {
    // Given
    final MissingServletRequestParameterException ex =
        new MissingServletRequestParameterException("param", "String");

    // When
    final Map<String, String> result = handler.handleMissingParams(ex);

    // Then
    assertEquals(result.get("error"), "Required request parameter 'param' is missing.");
  }

  @Test
  void shouldHandleDataIntegrityViolationExceptionWithRootCause() {
    // Given
    SQLException sqlException =
        new SQLException(
            "ERROR: duplicate key value violates unique constraint \"store_sap_store_id_key\"\n");
    final DataIntegrityViolationException ex =
        new DataIntegrityViolationException("conflict", sqlException);

    // When
    var response = handler.handleDataIntegrityViolationException(ex);

    // Then
    assertEquals(response.getStatusCode().value(), 409);
    assertEquals(
        response.getBody().getMessage(),
        "duplicate key value violates unique constraint \"store_sap_store_id_key\"");
  }

  @Test
  void shouldHandleDataIntegrityViolationExceptionWithoutRootCause() {
    // Given
    final DataIntegrityViolationException ex =
        new DataIntegrityViolationException("Generic DB error");

    // When
    var response = handler.handleDataIntegrityViolationException(ex);

    // Then
    assertEquals(response.getStatusCode().value(), 409);
    assertEquals(response.getBody().getMessage(), "Conflict: Record already exists.");
  }

  @Test
  void shouldHandleMethodArgumentNotValidException() {
    // Given
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError = new FieldError("store", "city", "City is required");
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
    final MethodArgumentNotValidException ex =
        new MethodArgumentNotValidException(null, bindingResult);

    // When
    final Map<String, String> errors = handler.handleValidationException(ex);

    // Then
    assertEquals(errors.get("city"), "City is required");
  }

  @Test
  void shouldHandleConstraintViolationException() {
    // Given
    final var path = mock(Path.class);
    final ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    when(violation.getPropertyPath()).thenReturn(path);
    when(path.toString()).thenReturn("latitude");
    when(violation.getMessage()).thenReturn("must not be null");

    final ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

    // When
    final Map<String, String> result = handler.handleConstraintViolationException(ex);

    // Then
    assertEquals(result.get("latitude"), "must not be null");
  }

  @Test
  void shouldHandleGenericException() {
    // Given
    final Exception ex = new RuntimeException("Unexpected error");

    // When
    final ErrorResponse result = handler.handleGeneric(ex);

    // Then
    assertEquals(result.getMessage(), "something went wrong, please try again later");
  }
}
