package com.skthvl.storeapi.controller;

import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.model.request.LoginRequest;
import com.skthvl.storeapi.model.response.LoginResponse;
import com.skthvl.storeapi.model.response.MessageResponse;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.service.AuthService;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller manages authentication-related actions for the API, including login and logout
 * functionality.
 *
 * <p>The `AuthController` handles requests to the `/api/v1/auth` endpoint and facilitates user
 * authentication by integrating with the `AuthService` and `JwtTokenProvider`. It also supports
 * token invalidation through the `InvalidatedTokenService`.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtTokenProvider jwtTokenProvider;
  private final InvalidatedTokenService invalidatedTokenService;

  /**
   * Constructs an instance of {@code AuthController}, initializing the required services for
   * authentication and token handling.
   *
   * @param authService the service responsible for user authentication and token generation
   * @param jwtTokenProvider the provider used to generate and validate JWT tokens
   * @param invalidatedTokenService the service managing invalidation of tokens during logout
   */
  public AuthController(
      final AuthService authService,
      final JwtTokenProvider jwtTokenProvider,
      final InvalidatedTokenService invalidatedTokenService) {
    this.authService = authService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.invalidatedTokenService = invalidatedTokenService;
  }

  /**
   * Authenticates the user using the provided credentials and generates an authentication token.
   *
   * @param request the request body containing the username and password for authentication
   * @return a {@link ResponseEntity} containing a {@link LoginResponse} with the generated token
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest request) {
    final var token =
        authService.authenticateAndGenerateToken(
            UserDto.builder()
                .userName(request.getUsername())
                .password(request.getPassword())
                .build());
    log.info("Login token generated: {}", token);

    return ResponseEntity.ok(new LoginResponse(token));
  }

  /**
   * Logs out the user by invalidating the provided access token. The token is extracted from the
   * "Authorization" header of the incoming HTTP request. If the token is missing or improperly
   * formatted, a bad request response is returned.
   *
   * @param request the HTTP request containing the "Authorization" header with the token
   * @return a {@link ResponseEntity} containing a {@link MessageResponse} with the result of the
   *     logout operation. If successful, a confirmation message is returned with an HTTP status of
   *     200 (OK). If the token is missing, a 400 (Bad Request) response is returned.
   */
  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logout(final HttpServletRequest request) {
    final var token = jwtTokenProvider.extractTokenFromHeader(request);

    if (token == null) {
      log.warn("token is missing.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new MessageResponse("token is missing"));
    }
    invalidatedTokenService.invalidateToken(token);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new MessageResponse("access token invalidated successfully"));
  }
}
