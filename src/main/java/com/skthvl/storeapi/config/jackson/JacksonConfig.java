package com.skthvl.storeapi.config.jackson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing Jackson JSON serialization/deserialization behavior.
 * Provides custom ObjectMapper and JTS module configuration for the application.
 */
@Configuration
public class JacksonConfig {
  /**
   * Configures and provides the main ObjectMapper bean with custom deserialization settings.
   * Ignores unknown JSON properties during deserialization to prevent mapping errors.
   *
   * @return configured ObjectMapper instance
   */
  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();

    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(jtsModule());
    mapper.registerModule(new JavaTimeModule());

    return mapper;
  }

  /**
   * Provides JTS (Java Topology Suite) module for Jackson to handle geographic types.
   * Enables serialization/deserialization of geometric objects used in spatial operations.
   *
   * @return JtsModule instance for geographic data handling
   */
  @Bean
  public JtsModule jtsModule() {
    return new JtsModule();
  }
}
