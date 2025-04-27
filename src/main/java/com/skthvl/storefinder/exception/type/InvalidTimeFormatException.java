package com.skthvl.storefinder.exception.type;

/**
 * Exception thrown when a time format is invalid or cannot be parsed correctly.
 */
public class InvalidTimeFormatException extends RuntimeException {

  /**
   * Constructs an InvalidTimeFormatException with the default message.
   */
  public InvalidTimeFormatException() {
    super("Invalid time format.");
  }

  /**
   * Constructs an InvalidTimeFormatException with a custom message.
   * @param message the detail message
   */
  public InvalidTimeFormatException(final String message) {
    super(message);
  }
}
