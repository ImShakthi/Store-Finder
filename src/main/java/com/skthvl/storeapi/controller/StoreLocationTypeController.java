package com.skthvl.storeapi.controller;

import com.skthvl.storeapi.entity.StoreLocationType;
import com.skthvl.storeapi.model.response.GetStoreLocationTypeResponse;
import com.skthvl.storeapi.repository.StoreLocationTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing store location type operations.
 * Provides endpoints for retrieving available store location types.
 */
@Slf4j
@RestController
public class StoreLocationTypeController {
  private final StoreLocationTypeRepository storeLocationTypeRepository;

  /**
   * Constructs a new StoreLocationTypeController with required dependencies.
   *
   * @param storeLocationTypeRepository Repository for accessing store location type data
   */
  public StoreLocationTypeController(
      final StoreLocationTypeRepository storeLocationTypeRepository) {
    this.storeLocationTypeRepository = storeLocationTypeRepository;
  }

  /**
   * Retrieves all available store location types.
   *
   * @return ResponseEntity containing a list of store location type names
   */
  @GetMapping("/api/v1/store-location-types")
  public ResponseEntity<GetStoreLocationTypeResponse> getStoreLocationTypes() {

    final var types =
        storeLocationTypeRepository.findAll().stream().map(StoreLocationType::getName).toList();

    log.info("types of store location are {}.", types.size());

    return ResponseEntity.ok(new GetStoreLocationTypeResponse(types));
  }
}
