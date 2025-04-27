package com.skthvl.storefinder.exception;

public class InvalidTimeFormatException extends RuntimeException {

  public InvalidTimeFormatException() {
    super("Invalid time format.");
  }

  public InvalidTimeFormatException(final String message) {
    super(message);
  }
}
