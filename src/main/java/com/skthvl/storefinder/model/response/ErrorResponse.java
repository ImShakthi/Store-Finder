package com.skthvl.storefinder.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

/**
 * ErrorResponse represents a standardized error response format for the API. It encapsulates error
 * messages that are returned to clients when exceptions or error conditions occur during request
 * processing.
 *
 * <p>This class is used across the application to maintain consistency in error reporting and is
 * typically returned within a {@link ResponseEntity} along with an appropriate HTTP status code.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private String message;
}
