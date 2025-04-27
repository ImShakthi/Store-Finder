package com.skthvl.storefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Store Finder application. Spring Boot application that provides
 * functionality for finding nearest stores based on geographical location.
 */
@SpringBootApplication
public class StoreFinderApplication {
  /**
   * Bootstrap the application.
   *
   * @param args Command line arguments passed to the application
   */
  public static void main(final String[] args) {
    SpringApplication.run(StoreFinderApplication.class, args);
  }
}
