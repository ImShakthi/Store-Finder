package com.skthvl.storefinder.exception.type;

/** Exception thrown when a time format is invalid or cannot be parsed correctly. */
public class StoreNotFoundException extends RuntimeException {

  /** Constructs an InvalidTimeFormatException with the default message. */
  public StoreNotFoundException() {
    super("store not found");
  }

  /**
   * Constructs an InvalidTimeFormatException with a custom message.
   *
   * @param message the detail message
   */
  public StoreNotFoundException(final String message) {
    super(message);
  }
}
