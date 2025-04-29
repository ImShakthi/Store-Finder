package com.skthvl.storeapi.controller;

import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.model.request.CreateUserRequest;
import com.skthvl.storeapi.model.request.DeleteUserRequest;
import com.skthvl.storeapi.model.response.MessageResponse;
import com.skthvl.storeapi.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController is a REST controller that manages user-related operations. It provides endpoints
 * for creating and deleting user accounts.
 *
 * <p>An instance of {@code UserAccountService} is used to handle the business logic related to user
 * management.
 *
 * <p>This controller uses validation annotations to ensure the correctness of the request payloads.
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

  private final UserAccountService userAccountService;

  public UserController(final UserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  /**
   * Creates a new user account based on the provided {@code CreateUserRequest}.
   *
   * @param request the request body containing the username, password, and roles for the new user
   * @return a {@link ResponseEntity} containing a {@link MessageResponse} with a success message
   *     and an HTTP status of 201 (Created)
   */
  @PostMapping
  public ResponseEntity<MessageResponse> createUser(
      @RequestBody @Valid final CreateUserRequest request) {
    userAccountService.registerUser(new UserDto(request.getUsername(), request.getPassword()));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new MessageResponse("user created successfully"));
  }

  /**
   * Deletes a user account based on the provided {@code DeleteUserRequest}.
   *
   * @param request the request body containing the username and password of the user account to be
   *     deleted
   * @return a {@link ResponseEntity} containing a {@link MessageResponse} with a success message
   *     and an HTTP status of 200 (OK) upon successful deletion
   */
  @DeleteMapping
  public ResponseEntity<MessageResponse> deleteUser(
      @RequestBody @Valid final DeleteUserRequest request) {
    userAccountService.deleteUser(
        UserDto.builder().userName(request.getUsername()).password(request.getPassword()).build());

    return ResponseEntity.ok(new MessageResponse("user deleted successfully"));
  }
}
