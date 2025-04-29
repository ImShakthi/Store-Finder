package com.skthvl.storeapi.exception.type;

/**
 * Exception thrown when user credentials are invalid during authentication or authorization
 * processes. This exception is typically used to indicate problems with user login attempts, token
 * validation, or access rights verification.
 */
public class InvalidCredentialException extends RuntimeException {
  /**
   * Constructs a new InvalidCredentialException with a default error message.
   */
  public InvalidCredentialException() {
    super("user credential is invalid!");
  }
  
  /**
   * Constructs a new InvalidCredentialException with the specified error message.
   *
   * @param message the detail message explaining the cause of the invalid credentials
   */
  public InvalidCredentialException(String message) {
    super(message);
  }
  
  /**
   * Constructs a new InvalidCredentialException with the specified error message and cause.
   *
   * @param message the detail message explaining the cause of the invalid credentials
   * @param cause the cause of this exception
   */
  public InvalidCredentialException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new InvalidCredentialException with the specified cause.
   *
   * @param cause the cause of this exception
   */
  public InvalidCredentialException(Throwable cause) {
    super(cause);
  }
}
