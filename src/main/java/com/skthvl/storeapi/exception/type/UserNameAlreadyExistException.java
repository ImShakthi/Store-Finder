package com.skthvl.storeapi.exception.type;


/**
 * Exception thrown when attempting to create or update a user with a username that already exists
 * in the system. This is a runtime exception used to handle duplicate username scenarios.
 */
public class UserNameAlreadyExistException extends RuntimeException {
  /**
   * Constructs a new UserNameAlreadyExistException with a default message.
   */
  public UserNameAlreadyExistException() {
    super("User name already exist!");
  }

  /**
   * Constructs a new UserNameAlreadyExistException with the specified detail message.
   *
   * @param message the detail message
   */
  public UserNameAlreadyExistException(String message) {
    super(message);
  }

  /**
   * Constructs a new UserNameAlreadyExistException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public UserNameAlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new UserNameAlreadyExistException with the specified cause.
   *
   * @param cause the cause of the exception
   */
  public UserNameAlreadyExistException(Throwable cause) {
    super(cause);
  }
}
