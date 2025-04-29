package com.skthvl.storeapi.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request object for creating a new user in the system. This class contains all necessary
 * information required for user creation and includes validation rules for each field.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
  /** The username for the new user account. Must not be blank or empty. */
  @NotBlank(message = "username must not be empty")
  private String username;

  /** The password for the new user account. Must not be blank or empty. */
  @NotBlank(message = "password must not be empty")
  private String password;
}
