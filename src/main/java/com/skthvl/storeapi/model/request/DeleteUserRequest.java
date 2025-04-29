package com.skthvl.storeapi.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request object used for user deletion operations.
 * Contains the necessary credentials (username and password) to authenticate
 * and authorize the deletion of a user account.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {
  /**
   * The username of the account to be deleted.
   * Must not be blank or empty.
   */
  @NotBlank(message = "username must not be empty")
  private String username;

  /**
   * The password for authentication.
   * Must not be blank or empty.
   */
  @NotBlank(message = "password must not be empty")
  private String password;
}
