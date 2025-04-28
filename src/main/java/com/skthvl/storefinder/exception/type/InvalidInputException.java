package com.skthvl.storefinder.exception.type;

/**
 * Exception thrown when a time format is invalid or cannot be parsed correctly.
 */
public class InvalidInputException extends RuntimeException {

  /**
   * Constructs an InvalidTimeFormatException with the default message.
   */
  public InvalidInputException() {
    super("Invalid input.");
  }

  /**
   * Constructs an InvalidTimeFormatException with a custom message.
   * @param message the detail message
   */
  public InvalidInputException(final String message) {
    super(message);
  }

}
