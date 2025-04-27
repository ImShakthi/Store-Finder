package com.skthvl.storefinder.controller;

import com.skthvl.storefinder.StoreService;
import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import com.skthvl.storefinder.model.request.LocationRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing store-related operations.
 * Provides endpoints for retrieving store information based on geographical location.
 */
@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {
  private final StoreService storeService;

  /**
   * Constructs a new StoreController with the required StoreService.
   *
   * @param storeService Service layer component for store operations
   */
  public StoreController(final StoreService storeService) {
    this.storeService = storeService;
  }

  /**
   * Retrieves a paginated list of stores sorted by distance from given coordinates.
   *
   * @param locationRequest Contains longitude and latitude coordinates
   * @param page Zero-based page index (default: 0)
   * @param size The size of the page to be returned (default: 5)
   * @return Page of StoreDistanceDto containing nearest stores with their distances
   */
  @PostMapping("/nearest")
  public Page<StoreDistanceDto> getNearestStores(
      @Valid @RequestBody final LocationRequest locationRequest,
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "5") final int size) {
    return storeService.findNearestStores(
        locationRequest.getLongitude(), locationRequest.getLatitude(), page, size);
  }
}
