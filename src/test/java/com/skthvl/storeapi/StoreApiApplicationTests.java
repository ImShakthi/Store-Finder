package com.skthvl.storeapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.skthvl.storeapi.repository.StoreRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreApiApplicationTests {

  private static final DockerImageName POSTGIS_IMAGE =
      DockerImageName.parse("postgis/postgis:17-3.4").asCompatibleSubstituteFor("postgres");

  static final PostgreSQLContainer<?> POSTGIS = new PostgreSQLContainer<>(POSTGIS_IMAGE);
  static final GenericContainer<?> REDIS =
      new GenericContainer<>(DockerImageName.parse("redis:7.0-alpine")).withExposedPorts(6379);

  private static final String BASE_STORE_URL = "/api/v1/stores";
  private static final String BASE_CITY_URL = "/api/v1/cities";
  private static final String BASE_STORE_LOCATION_TYPE_URL = "/api/v1/store-location-types";

  @Autowired private StoreRepository storeRepository;

  @LocalServerPort private int port;

  @BeforeAll
  static void startContainer() {
    POSTGIS.start();
    REDIS.start();
  }

  @AfterAll
  static void stopContainer() {
    POSTGIS.stop();
    REDIS.stop();
  }

  @DynamicPropertySource
  static void configureDatasource(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGIS::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGIS::getUsername);
    registry.add("spring.datasource.password", POSTGIS::getPassword);
    registry.add("spring.data.redis.host", REDIS::getHost);
    registry.add("spring.data.redis.port", REDIS.getMappedPort(6379)::toString);
  }

  @BeforeEach
  void configureRestAssured() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  void contextLoads() {
    // Basic context load check
  }

  @Test
  void shouldCreateAndDeleteStoreSuccessfully() {
    final var requestBody = getTestStoreRequestBody();
    final var token = getAdminToken();

    final var storeId =
        given()
            .header("Authorization", "Bearer " + token)
            .basePath(BASE_STORE_URL)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(requestBody)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .path("storeId")
            .toString();

    assertTrue(
        storeRepository.findByStoreId(storeId).isPresent(), "Store should exist after creation.");

    given()
        .header("Authorization", "Bearer " + token)
        .basePath(BASE_STORE_URL + "/{storeId}")
        .pathParam("storeId", storeId)
        .when()
        .delete()
        .then()
        .statusCode(200)
        .body("message", equalTo("store details is deleted."));

    assertFalse(
        storeRepository.findByStoreId(storeId).isPresent(),
        "Store should not exist after deletion.");
  }

  @Test
  void shouldReturnCorrectCitiesAndLocationTypesAndStoreStatus() {
    given()
        .basePath(BASE_CITY_URL)
        .accept(ContentType.JSON)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("noOfCities", equalTo(375));

    final var actualLocationTypeResponse =
        given()
            .basePath(BASE_STORE_LOCATION_TYPE_URL)
            .accept(ContentType.JSON)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .asString();

    final var expectedLocationTypes = "{\"types\":[\"SupermarktPuP\",\"Supermarkt\",\"PuP\"]}";
    assertEquals(expectedLocationTypes, actualLocationTypeResponse);

    given()
        .basePath(BASE_STORE_URL + "/{storeId}/operation-status")
        .pathParam("storeId", "EOgKYx4XFiQAAAFJa_YYZ4At")
        .accept(ContentType.JSON)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("storeId", equalTo("EOgKYx4XFiQAAAFJa_YYZ4At"))
        // Test can run on any time of the day, so checking for both OPEN and CLOSED
        .body("status", anyOf(equalTo("OPEN"), equalTo("CLOSED")))
        .body("openingTime", equalTo("08:00"))
        .body("closingTime", equalTo("20:00"));
  }

  @Test
  void shouldReturnNearbyStoresBasedOnRadius() {
    double longitude = 4.745031;
    double latitude = 52.633740;
    String nearbyUrl = BASE_STORE_URL + "/nearby";

    validateNearbyStoresResponse(
        nearbyUrl,
        longitude,
        latitude,
        null,
        "data/testdata/nearby-stores-response-without-radius-input.json");

    validateNearbyStoresResponse(
        nearbyUrl,
        longitude,
        latitude,
        "800m",
        "data/testdata/nearby-stores-response-with-radius-800m-input.json");

    validateNearbyStoresResponse(
        nearbyUrl,
        longitude,
        latitude,
        "10km",
        "data/testdata/nearby-stores-response-with-radius-10km-input.json",
        10);
  }

  private void validateNearbyStoresResponse(
      final String url,
      final double longitude,
      final double latitude,
      final String radius,
      final String expectedFile) {
    validateNearbyStoresResponse(url, longitude, latitude, radius, expectedFile, null);
  }

  private void validateNearbyStoresResponse(
      final String url,
      final double longitude,
      final double latitude,
      final String radius,
      final String expectedFile,
      final Integer size) {
    final var request =
        given()
            .basePath(url)
            .queryParam("longitude", longitude)
            .queryParam("latitude", latitude)
            .accept(ContentType.JSON);

    if (radius != null) {
      request.queryParam("radius", radius);
    }
    if (size != null) {
      request.queryParam("size", size);
    }

    final String actualResponse = request.when().get().then().statusCode(200).extract().asString();

    final String expectedResponse = getContentFromFile(expectedFile);
    assertEquals(
        expectedResponse, actualResponse, "Nearby stores response does not match expected.");
  }

  private String getTestStoreRequestBody() {
    return """
            {
              "city": "Amsterdam",
              "postalCode": "7041 JE",
              "street": "Stadsplein",
              "street2": "71",
              "street3": "",
              "addressName": "Jumbo Amsterdam test",
              "longitude": "6.245829",
              "latitude": "51.874272",
              "complexNumber": "30171",
              "showWarningMessage": true,
              "todayOpen": "08:00",
              "locationType": "Supermarkt",
              "sapStoreId": "467681",
              "todayClose": "21:00"
            }
        """;
  }

  private String getAdminToken() {
    String json = "{\"username\":\"admin\",\"password\":\"admin123\"}";
    return given()
        .basePath("/api/v1/auth/login")
        .contentType(ContentType.JSON)
        .body(json)
        .when()
        .post()
        .then()
        .extract()
        .path("token");
  }

  private String getContentFromFile(final String filePath) {
    try {
      final Path path =
          Paths.get(
              Objects.requireNonNull(getClass().getClassLoader().getResource(filePath)).toURI());
      return Files.readString(path, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Error reading file: " + filePath, e);
    }
  }
}
