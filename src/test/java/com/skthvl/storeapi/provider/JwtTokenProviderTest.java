package com.skthvl.storeapi.provider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {
  private static final String SECRET_KEY =
      Base64.getEncoder()
          .encodeToString("verysecurekey123456789012345678901".getBytes()); // 32 bytes

  private static final long EXPIRATION = 3600000L; // 1 hour

  private JwtTokenProvider jwtTokenProvider;

  @BeforeEach
  void setUp() {
    jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXPIRATION);
  }

  @Test
  void extractTokenFromHeader_shouldReturnToken_whenBearerPresent() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer abc.def.ghi");

    String token = jwtTokenProvider.extractTokenFromHeader(request);

    assertEquals("abc.def.ghi", token);
  }

  @Test
  void extractTokenFromHeader_shouldReturnNull_whenHeaderMissingOrInvalid() {
    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getHeader("Authorization")).thenReturn(null);
    assertNull(jwtTokenProvider.extractTokenFromHeader(request));

    when(request.getHeader("Authorization")).thenReturn("InvalidToken abc.def");
    assertNull(jwtTokenProvider.extractTokenFromHeader(request));
  }

  @Test
  void generateToken_and_extractUsername_shouldWork() {
    String subject = "test.user";
    final String token = jwtTokenProvider.generateToken(subject);

    final String extracted = jwtTokenProvider.extractUsername(token);

    assertEquals(subject, extracted);
  }

  @Test
  void generateToken_and_extractJti_shouldReturnNonNullId() {
    String token = jwtTokenProvider.generateToken("user");

    assertNotNull(jwtTokenProvider.extractJtiId(token));
  }

  @Test
  void validateToken_shouldReturnTrue_whenValidToken() {
    String token = jwtTokenProvider.generateToken("validUser");

    assertTrue(jwtTokenProvider.validateToken(token));
  }

  @Test
  void validateToken_shouldReturnFalse_whenTokenExpired() {
    final String subject = "expiredUser";

    final Date pastDate = new Date(System.currentTimeMillis() - EXPIRATION - 1000);
    final String token = jwtTokenProvider.generateToken(subject, pastDate);

    boolean result = jwtTokenProvider.validateToken(token);

    assertFalse(result);
  }
}
