package com.skthvl.storeapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Cache-backed service for managing authentication token invalidation. Uses Spring Cache for
 * efficient token status tracking.
 */
@Slf4j
@Service
public class InvalidatedTokenService {

  /** Initializes service with Spring Cache integration. */
  public InvalidatedTokenService() {}

  /**
   * Marks a token as invalid.
   *
   * @param token JWT to invalidate
   * @return true when invalidated
   */
  @CachePut(value = "invalidated-tokens", key = "#token")
  public boolean invalidateToken(final String token) {
    log.debug("invalidating token: {}", token);
    return true;
  }

  /**
   * Checks token validity status.
   *
   * @param token JWT to verify
   * @return true if invalid, false if valid
   */
  @Cacheable(value = "invalidated-tokens", key = "#token")
  public boolean isTokenInvalidated(final String token) {
    log.debug("checking if token is invalidated: {}", token);

    // by default, token is not invalidated
    return false;
  }
}
