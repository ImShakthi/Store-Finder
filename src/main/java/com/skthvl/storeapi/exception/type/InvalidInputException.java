package com.skthvl.storeapi.exception.type;

/** Exception thrown when the provided input is invalid or cannot be processed. */
public class InvalidInputException extends RuntimeException {

  /** Constructs an InvalidInputException with the default message. */
  public InvalidInputException() {
    super("Invalid input.");
  }

  /**
   * Constructs an InvalidInputException with a custom message.
   *
   * @param message the detail message explaining the reason for invalid input
   */
  public InvalidInputException(final String message) {
    super(message);
  }
}
