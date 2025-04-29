package com.skthvl.storeapi.controller;

import com.skthvl.storeapi.entity.City;
import com.skthvl.storeapi.model.response.GetCitiesResponse;
import com.skthvl.storeapi.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing city-related operations. */
@Slf4j
@RestController
public class CityController {
  private final CityRepository cityRepository;

  /**
   * Constructs CityController with required repository dependency.
   *
   * @param cityRepository repository for city data access
   */
  public CityController(final CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  /**
   * Retrieves a list of all cities where stores operate.
   *
   * @return ResponseEntity containing list of city names
   */
  @GetMapping("/api/v1/cities")
  public ResponseEntity<GetCitiesResponse> getCities() {

    final var cities = cityRepository.findAll().stream().map(City::getName).toList();

    log.info("store operates in {} cities.", cities.size());

    return ResponseEntity.ok(new GetCitiesResponse(cities.size(), cities));
  }
}
