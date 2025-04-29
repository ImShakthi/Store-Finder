package com.skthvl.storeapi.controller;

import com.skthvl.storeapi.model.dto.StoreDistanceDto;
import com.skthvl.storeapi.model.response.PaginatedResponse;
import com.skthvl.storeapi.model.response.StoreOperationStatusResponse;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller interface for managing store information and operations. Provides endpoints for
 * retrieving store locations and operational status.
 */
public interface StoreInfoController {

  /**
   * Retrieves a paginated list of stores nearest to a specific geographical location based on
   * provided longitude, latitude, and an optional search radius.
   *
   * @param longitude The longitude of the reference location. Must be between -180.0 and 180.0.
   * @param latitude The latitude of the reference location. Must be between -90.0 and 90.0.
   * @param radius Optional search radius around the reference location. Defaults to 0, indicating
   *     no radius filter. Can be specified in meters (e.g., "500m") or kilometers (e.g., "2km").
   * @param page The page index for pagination. Defaults to 0.
   * @param size The number of records per page for pagination. Defaults to 5.
   * @return A response entity containing a paginated response with a list of stores and their
   *     respective distance from the reference location.
   */
  @GetMapping(value = "/api/v1/stores/nearby", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<PaginatedResponse<StoreDistanceDto>> getNearestStores(
      @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
          @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
          @RequestParam(name = "longitude")
          final Double longitude,
      @NotNull(message = "Latitude is required")
          @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
          @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
          @RequestParam(name = "latitude")
          final Double latitude,
      @RequestParam(name = "radius", defaultValue = "0") final String radius,
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "5") final int size);

  /**
   * Retrieves the current operational status of a specific store.
   *
   * @param storeId Unique identifier of the store
   * @return Response containing the store's operational status information
   */
  @GetMapping("/api/v1/stores/{storeId}/operation-status")
  ResponseEntity<StoreOperationStatusResponse> getStoreOperationStatus(
      @PathVariable(name = "storeId") final String storeId);
}
