package com.skthvl.storeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.storeapi.entity.UserAccount;
import com.skthvl.storeapi.exception.type.InvalidCredentialException;
import com.skthvl.storeapi.exception.type.UserDoesNotExistException;
import com.skthvl.storeapi.exception.type.UserNameAlreadyExistException;
import com.skthvl.storeapi.model.dto.UserDto;
import com.skthvl.storeapi.repository.UserAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserAccountRepository userAccountRepository;

  private UserAccountService userAccountService;

  @BeforeEach
  void setUp() {
    userAccountService = new UserAccountService(passwordEncoder, userAccountRepository);
  }

  @Test
  void registerUser_shouldSaveNewUser_whenUsernameDoesNotExist() {
    UserDto userDto = UserDto.builder().userName("test_user_name").password("secret").build();

    when(userAccountRepository.existsByName("test_user_name")).thenReturn(false);
    when(passwordEncoder.encode("secret")).thenReturn("hashed-secret");

    userAccountService.registerUser(userDto);

    ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
    verify(userAccountRepository).save(captor.capture());
    assertEquals("test_user_name", captor.getValue().getName());
    assertEquals("hashed-secret", captor.getValue().getPasswordHash());
  }

  @Test
  void registerUser_shouldThrowException_whenUsernameExists() {
    UserDto userDto = UserDto.builder().userName("test_user_name").password("secret").build();

    when(userAccountRepository.existsByName("test_user_name")).thenReturn(true);

    UserNameAlreadyExistException ex =
        assertThrows(
            UserNameAlreadyExistException.class, () -> userAccountService.registerUser(userDto));

    assertEquals("user name already exists: test_user_name", ex.getMessage());
    verify(userAccountRepository, never()).save(any());
  }

  @Test
  void getValid_UserByCredential_shouldReturnTrue_whenCredentialsMatch() {
    UserDto userDto =
        UserDto.builder()
            .userName("test_user_name")
            .password("secret")
            .build();
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed-secret");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("secret", "hashed-secret")).thenReturn(true);

    final UserAccount validUser = userAccountService.getValidUserByCredential(userDto);

    assertNotNull(validUser);
    assertEquals(validUser.getName(), user.getName());
    assertEquals(validUser.getPasswordHash(), user.getPasswordHash());
  }

  @Test
  void isUserCredentialValid_shouldThrowException_whenUserNotFoundUserByCredential() {
    when(userAccountRepository.findByName("ghost")).thenReturn(Optional.empty());

    final var user = UserDto.builder().userName("ghost").password("pass").build();
    assertThrows(
        UserDoesNotExistException.class, () -> userAccountService.getValidUserByCredential(user));
  }

  @Test
  void getValid_UserByCredential_shouldThrowException_whenPasswordMismatch() {
    final var userWithWrongPassword =
        UserDto.builder().userName("test_user_name").password("wrong").build();
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

    assertThrows(
        InvalidCredentialException.class,
        () -> userAccountService.getValidUserByCredential(userWithWrongPassword));
  }

  @Test
  void deleteUser_shouldDeleteUser_whenCredentialsMatch() {
    UserDto userDto = UserDto.builder().userName("test_user_name").password("secret").build();
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);

    userAccountService.deleteUser(userDto);

    verify(userAccountRepository).delete(user);
  }

  @Test
  void deleteUser_shouldThrowException_whenUserNotFound() {
    when(userAccountRepository.findByName("ghost")).thenReturn(Optional.empty());

    final var user = UserDto.builder().userName("ghost").password("any").build();
    assertThrows(UserDoesNotExistException.class, () -> userAccountService.deleteUser(user));
  }

  @Test
  void deleteUser_shouldThrowException_whenPasswordMismatch() {
    UserDto userDto = UserDto.builder().userName("test_user_name").password("wrong").build();
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

    assertThrows(InvalidCredentialException.class, () -> userAccountService.deleteUser(userDto));

    verify(userAccountRepository, never()).delete(any());
  }
}
