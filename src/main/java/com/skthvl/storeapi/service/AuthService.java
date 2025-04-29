package com.skthvl.storeapi.service;

import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user authentication and JWT token generation. This service coordinates
 * between user account validation and JWT token creation.
 */
@Service
public class AuthService {
  private final UserAccountService userAccountService;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * Constructs AuthService with required dependencies.
   *
   * @param userAccountService service for user account operations
   * @param jwtTokenProvider provider for JWT token generation
   */
  public AuthService(
      final UserAccountService userAccountService, final JwtTokenProvider jwtTokenProvider) {
    this.userAccountService = userAccountService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  /**
   * Authenticates user credentials and generates a JWT token.
   *
   * @param userDto the user credentials to authenticate
   * @return JWT token string for the authenticated user
   */
  public String authenticateAndGenerateToken(final UserDto userDto) {
    final var user = userAccountService.getValidUserByCredential(userDto);
    return jwtTokenProvider.generateTokenWithRoles(userDto.userName());
  }
}
