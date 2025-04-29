package com.skthvl.storeapi.exception.type;

/**
 * Exception thrown when an operation is attempted on a user that does not exist in the system. This
 * runtime exception is used to indicate user lookup or access failures.
 */
public class UserDoesNotExistException extends RuntimeException {
  /** Constructs a new UserDoesNotExistException with a default message. */
  public UserDoesNotExistException() {
    super("User doesn't exist!");
  }

  /**
   * Constructs a new UserDoesNotExistException with the specified detail message.
   *
   * @param message the detail message
   */
  public UserDoesNotExistException(String message) {
    super(message);
  }

  /**
   * Constructs a new UserDoesNotExistException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public UserDoesNotExistException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new UserDoesNotExistException with the specified cause.
   *
   * @param cause the cause of the exception
   */
  public UserDoesNotExistException(Throwable cause) {
    super(cause);
  }
}
