package com.skthvl.storeapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response object representing the result of a successful login attempt. This class encapsulates
 * the JWT (JSON Web Token) that is generated upon successful authentication and is used for
 * subsequent authorized requests.
 *
 * <p>The response contains a single field 'token' which holds the JWT string. This token should be
 * included in the Authorization header of subsequent HTTP requests using the Bearer scheme.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
  /**
   * The JWT (JSON Web Token) string generated upon successful authentication. This token contains
   * encoded user information and should be included in subsequent requests as a Bearer token in the
   * Authorization header.
   */
  private String token;
}
