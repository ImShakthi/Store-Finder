package com.skthvl.storeapi.service;

import com.skthvl.storeapi.entity.UserAccount;
import com.skthvl.storeapi.exception.type.InvalidCredentialException;
import com.skthvl.storeapi.exception.type.UserDoesNotExistException;
import com.skthvl.storeapi.exception.type.UserNameAlreadyExistException;
import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for managing user account operations including registration,
 * authentication, and deletion. This service handles all business logic related to user
 * account management and ensures data consistency.
 */
@Slf4j
@Service
public class UserAccountService {
  private final PasswordEncoder passwordEncoder;
  private final UserAccountRepository userAccountRepository;

  /**
   * Constructs a new UserAccountService with required dependencies.
   *
   * @param passwordEncoder Service for encoding and matching passwords
   * @param userAccountRepository Repository for user account persistence operations
   */
  public UserAccountService(
      final PasswordEncoder passwordEncoder, final UserAccountRepository userAccountRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userAccountRepository = userAccountRepository;
  }

  /**
   * Registers a new user in the system.
   *
   * @param userDto DTO containing user registration information
   * @throws UserNameAlreadyExistException if the username is already taken
   */
  @Transactional
  public void registerUser(final UserDto userDto) {
    if (userAccountRepository.existsByName(userDto.userName())) {
      log.warn("User name already exists: {}", userDto.userName());
      throw new UserNameAlreadyExistException(
          String.format("user name already exists: %s", userDto.userName()));
    }

    final UserAccount userAccount =
        UserAccount.builder()
            .name(userDto.userName())
            .passwordHash(passwordEncoder.encode(userDto.password()))
            .build();

    userAccountRepository.save(userAccount);
    log.info("User registered successfully: {}", userDto.userName());
  }

  /**
   * Validates user credentials and returns the corresponding user account.
   *
   * @param userDto DTO containing user credentials
   * @return UserAccount if credentials are valid
   * @throws UserDoesNotExistException if user is not found
   * @throws InvalidCredentialException if credentials are invalid
   */
  @Transactional(readOnly = true)
  public UserAccount getValidUserByCredential(final UserDto userDto) {
    return validateCredentials(userDto);
  }

  /**
   * Deletes a user account after validating their credentials.
   *
   * @param userDto DTO containing user credentials
   * @throws UserDoesNotExistException if user is not found
   * @throws InvalidCredentialException if credentials are invalid
   */
  @Transactional
  public void deleteUser(final UserDto userDto) {
    final var userAccount = validateCredentials(userDto);

    userAccountRepository.delete(userAccount);
    log.info("User account deleted successfully: {}", userDto.userName());
  }

  private UserAccount validateCredentials(final UserDto userDto) {
    final var userAccount =
        userAccountRepository
            .findByName(userDto.userName())
            .orElseThrow(UserDoesNotExistException::new);

    if (!passwordEncoder.matches(userDto.password(), userAccount.getPasswordHash())) {
      throw new InvalidCredentialException();
    }
    log.info("User credential validated successfully: {}", userDto.userName());

    return userAccount;
  }
}
