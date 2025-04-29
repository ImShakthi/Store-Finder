package com.skthvl.storeapi;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreApiApplicationTests {

  private static final DockerImageName postgisImage =
      DockerImageName.parse("postgis/postgis:17-3.4").asCompatibleSubstituteFor("postgres");
  static final PostgreSQLContainer postgis = new PostgreSQLContainer<>(postgisImage);

  @LocalServerPort private Integer port;

  @BeforeAll
  static void beforeAll() {
    postgis.start();
  }

  @AfterAll
  static void afterAll() {
    postgis.stop();
  }

  @DynamicPropertySource
  static void configureProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgis::getJdbcUrl);
    registry.add("spring.datasource.username", postgis::getUsername);
    registry.add("spring.datasource.password", postgis::getPassword);
  }

  @BeforeEach
  void backEach() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  void contextLoads() {}
}
