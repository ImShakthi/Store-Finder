package com.skthvl.storeapi.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a login request containing user credentials for authentication. This class is used as
 * a request body for login endpoints in the API.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
  /** The username of the user attempting to log in. This field must not be blank or empty. */
  @NotBlank(message = "username must not be empty")
  private String username;

  /** The password associated with the user's account. This field must not be blank or empty. */
  @NotBlank(message = "password must not be empty")
  private String password;
}
