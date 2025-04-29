package com.skthvl.storeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.storeapi.entity.UserAccount;
import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserAccountService userAccountService;
  @Mock private JwtTokenProvider jwtTokenProvider;

  private AuthService authService;

  @BeforeEach
  void setUp() {
    authService = new AuthService(userAccountService, jwtTokenProvider);
  }

  @Test
  void authenticateAndGenerateToken_shouldReturnToken_whenCredentialsAreValid() {
    // Given
    final var userDto =
        UserDto.builder().userName("test_user_name").password("securePassword123").build();
    final var userAccount = UserAccount.builder().name("test_user_name").build();

    when(userAccountService.getValidUserByCredential(userDto)).thenReturn(userAccount);
    when(jwtTokenProvider.generateTokenWithRoles("test_user_name"))
        .thenReturn("mocked-jwt-token");

    // When
    final String token = authService.authenticateAndGenerateToken(userDto);

    // Then
    assertEquals("mocked-jwt-token", token);
    verify(userAccountService).getValidUserByCredential(userDto);
    verify(jwtTokenProvider).generateTokenWithRoles("test_user_name");
  }

  @Test
  void authenticateAndGenerateToken_shouldReturnEmptyString_whenCredentialsAreInvalid() {
    // Given
    final var userDto =
        UserDto.builder().userName("test_user_name").password("wrongPassword").build();
    final var userAccount = UserAccount.builder().name("test_user_name").build();

    when(userAccountService.getValidUserByCredential(userDto)).thenReturn(userAccount);

    // When
    final String token = authService.authenticateAndGenerateToken(userDto);

    // Then
    assertNull(token);
    verify(userAccountService).getValidUserByCredential(userDto);
    verify(jwtTokenProvider, never()).generateToken(any());
  }
}
