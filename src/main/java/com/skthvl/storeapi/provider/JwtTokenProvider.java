package com.skthvl.storeapi.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for generating, extracting, and validating JWT (JSON Web Token) tokens.
 * It provides methods to generate tokens for users with roles, extract data from tokens, and
 * validate tokens. The secret key for signing the tokens and their expiration duration are
 * configured through application properties.
 */
@Slf4j
@Component
public class JwtTokenProvider {

  private final String jwtSecret;
  private final long jwtExpirationInMilliseconds;

  public JwtTokenProvider(
      @Value("${store-api.jwt.secret}") final String jwtSecret,
      @Value("${store-api.jwt.expiration-in-milliseconds}")
          final long jwtExpirationInMilliseconds) {
    this.jwtSecret = jwtSecret;
    this.jwtExpirationInMilliseconds = jwtExpirationInMilliseconds;
  }

  /**
   * Extracts the token from the "Authorization" header of an HTTP request. The method expects the
   * token to be in the format "Bearer token" and returns the token value without the "Bearer "
   * prefix. If the header is missing, or does not start with the expected prefix, it returns null.
   *
   * @param request the HttpServletRequest containing the "Authorization" header
   * @return the extracted token as a String, or null if the header is missing or improperly
   *     formatted
   */
  public String extractTokenFromHeader(final HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

  /**
   * Generates a JSON Web Token (JWT) containing the specified subject, roles, and an issue date.
   * The token is created with a timestamp based on the provided current date and an expiration
   * period defined by the class's configuration. Each token includes a unique identifier (JTI).
   *
   * @param subject the subject for which the token is being generated, typically a username
   * @param currentDate the current date used as the token's issue date
   * @return a signed JWT as a String, containing the subject, roles, issue date, and a unique
   *     identifier
   */
  public String generateToken(final String subject, final Date currentDate) {
    final Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMilliseconds);

    return Jwts.builder()
        .subject(subject)
        .issuedAt(currentDate)
        .expiration(expireDate)
        .id(UUID.randomUUID().toString())
        .signWith(key())
        .compact();
  }

  /**
   * Generates a JSON Web Token (JWT) containing the specified subject and roles. The token is
   * created with the current timestamp and an expiration period defined in the containing class's
   * configuration.
   *
   * @param subject the subject for which the token is being generated, typically a username
   * @return a signed JWT as a String containing the subject and roles
   */
  public String generateTokenWithRoles(final String subject) {
    return generateToken(subject, new Date(System.currentTimeMillis()));
  }

  /**
   * Extracts the username (subject) from the provided JSON Web Token (JWT).
   *
   * @param token the JWT from which to extract the username
   * @return the username extracted from the token as a String
   */
  public String extractUsername(final String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  /** Extracts the claims from the provided JSON Web Token (JWT). This method parses */
  public Claims extractClaims(final String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Extracts the unique identifier (JTI) from the provided JSON Web Token (JWT).
   *
   * @param token the JWT from which to extract the JTI
   * @return the unique identifier (JTI) extracted from the token as a String
   */
  public String extractJtiId(final String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getId();
  }

  /**
   * Validates a JSON Web Token (JWT) for its authenticity and expiration status. The method parses
   * the provided token and checks if it's valid.
   *
   * @param token the JWT as a String to be validated
   * @return true if the token is valid; false if the token is expired or invalid
   */
  public boolean validateToken(final String token) {
    try {
      Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
    } catch (ExpiredJwtException ex) {
      return false;
    }
    return true;
  }

  /**
   * Retrieves the cryptographic signing key for JSON Web Token (JWT) operations. This key is
   * derived from a Base64-decoded secret configured in the application.
   *
   * @return the cryptographic signing key used for signing and verifying JWTs
   */
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
