package com.skthvl.storeapi.service;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = InvalidatedTokenServiceTest.TestConfig.class)
class InvalidatedTokenServiceTest {

  @Autowired private InvalidatedTokenService invalidatedTokenService;

  @Autowired private CacheManager cacheManager;

  @Test
  void testInvalidateToken_shouldStoreTokenInCache() {
    final String token = "abc123";

    // Call to cache it
    boolean result = invalidatedTokenService.invalidateToken(token);
    assertTrue(result);

    final Boolean cached = cacheManager.getCache("invalidated-tokens").get(token, Boolean.class);

    assertNotNull(cached);
    assertTrue(cached);
  }

  @Test
  void testIsTokenInvalidated_shouldReturnFalseWhenTokenNotInCache() {
    final String token = "not-present-token";

    final boolean result = invalidatedTokenService.isTokenInvalidated(token);

    assertFalse(result);
  }

  @Test
  void testIsTokenInvalidated_shouldReturnTrueIfTokenPreviouslyInvalidated() {
    final String token = "reused-token";

    // Should return false initially
    assertFalse(invalidatedTokenService.isTokenInvalidated(token));

    // First invalidate it
    invalidatedTokenService.invalidateToken(token);

    // Should return true now
    assertTrue(invalidatedTokenService.isTokenInvalidated(token));
  }

  @Configuration
  @EnableCaching
  static class TestConfig {

    @Bean
    public InvalidatedTokenService invalidatedTokenService() {
      return new InvalidatedTokenService();
    }

    @Bean
    public CacheManager cacheManager() {
      return new org.springframework.cache.concurrent.ConcurrentMapCacheManager(
          "invalidated-tokens");
    }
  }
}
