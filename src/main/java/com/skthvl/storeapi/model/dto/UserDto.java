package com.skthvl.storeapi.model.dto;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing user information for authentication and authorization
 * purposes. This record encapsulates user credentials, providing a clean interface for transferring
 * user data between different layers of the application.
 */
@Builder
public record UserDto(String userName, String password) {}
