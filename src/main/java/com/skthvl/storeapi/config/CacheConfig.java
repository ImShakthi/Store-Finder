package com.skthvl.storeapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/** Configuration class for setting up cache management in the application using Redis. */
@Configuration
public class CacheConfig {

  @Value("${store-api.cache.expiration-in-minutes:3000}")
  private int expirationTime;

  private final RedisConnectionFactory connectionFactory;

  public CacheConfig(final RedisConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  /**
   * Configures and provides a RedisCacheManager bean for managing application caching.
   *
   * <p>This method sets up and returns a RedisCacheManager instance with custom configurations: -
   * Values are serialized and deserialized using Jackson2JsonRedisSerializer with ObjectMapper
   * configured for type information. - Cache entries have a time-to-live (TTL) based on the
   * expirationTime property. - Caching null values is disabled to ensure efficient storage.
   *
   * @return a configured {@link RedisCacheManager} instance for managing Redis-based caching.
   */
  @Bean
  public RedisCacheManager cacheManager() {
    final ObjectMapper objectMapper =
        new ObjectMapper()
            .activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    final Jackson2JsonRedisSerializer<Object> jsonSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

    final RedisSerializationContext.SerializationPair<Object> pair =
        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);

    final RedisCacheConfiguration config =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(pair)
            .entryTtl(Duration.ofMinutes(expirationTime))
            .disableCachingNullValues();

    return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
  }
}
