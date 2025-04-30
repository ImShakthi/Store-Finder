package com.skthvl.storeapi.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.storeapi.config.SecurityConfig;
import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.model.request.CreateUserRequest;
import com.skthvl.storeapi.model.request.DeleteUserRequest;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.service.AuthService;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import com.skthvl.storeapi.service.UserAccountService;
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
@WebMvcTest(UserController.class)
class UserControllerMockMvcTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private AuthService authService;
  @MockitoBean private JwtTokenProvider jwtTokenProvider;
  @MockitoBean private InvalidatedTokenService invalidatedTokenService;
  @MockitoBean private UserAccountService userAccountService;

  @Test
  void createUser_shouldReturnCreated() throws Exception {
    // Given
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("testuser");
    request.setPassword("password123");

    doNothing().when(userAccountService).registerUser(new UserDto("testuser", "password123"));

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("user created successfully"));

    verify(userAccountService).registerUser(new UserDto("testuser", "password123"));
  }

  @Test
  @DisplayName("Given valid user data, when deleteUser is called, then return 200 OK")
  void deleteUser_shouldReturnOk() throws Exception {
    // Given
    DeleteUserRequest request = new DeleteUserRequest();
    request.setUsername("testuser");
    request.setPassword("password123");

    doNothing()
        .when(userAccountService)
        .deleteUser(UserDto.builder().userName("testuser").password("password123").build());

    // When & Then
    mockMvc
        .perform(
            delete("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("user deleted successfully"));

    verify(userAccountService)
        .deleteUser(UserDto.builder().userName("testuser").password("password123").build());
  }
}
