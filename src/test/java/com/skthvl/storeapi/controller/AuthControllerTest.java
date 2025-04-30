package com.skthvl.storeapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.storeapi.config.SecurityConfig;
import com.skthvl.storeapi.model.request.LoginRequest;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.service.AuthService;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerMockMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AuthService authService;
  @MockitoBean private JwtTokenProvider jwtTokenProvider;
  @MockitoBean private InvalidatedTokenService invalidatedTokenService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Given valid credentials, when login is called, then return token")
  void login_withValidCredentials_returnsToken() throws Exception {
    // Given
    final var loginRequest = new LoginRequest("test_user_name", "securePassword123");

    final var expectedToken = "mocked-jwt-token";
    when(authService.authenticateAndGenerateToken(any())).thenReturn(expectedToken);

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(expectedToken));

    verify(authService, times(1)).authenticateAndGenerateToken(any());
  }

  @Test
  @DisplayName("Given valid token, when logout is called, then token is invalidated")
  void logout_withValidToken_invalidatesToken() throws Exception {
    // Given
    String validToken = "Bearer valid-token";
    when(jwtTokenProvider.extractTokenFromHeader(any(HttpServletRequest.class)))
        .thenReturn("valid-token");

    // When & Then
    mockMvc
        .perform(post("/api/v1/auth/logout").header("Authorization", validToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("access token invalidated successfully"));

    verify(jwtTokenProvider).extractTokenFromHeader(any());
    verify(invalidatedTokenService).invalidateToken(eq("valid-token"));
  }

  @Test
  @DisplayName("Given no token, when logout is called, then return bad request")
  void logout_withMissingToken_returnsBadRequest() throws Exception {
    // Given
    when(jwtTokenProvider.extractTokenFromHeader(any(HttpServletRequest.class))).thenReturn(null);

    // When & Then
    mockMvc
        .perform(post("/api/v1/auth/logout"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("token is missing"));

    verify(jwtTokenProvider).extractTokenFromHeader(any());
    verify(invalidatedTokenService, never()).invalidateToken(any());
  }
}
