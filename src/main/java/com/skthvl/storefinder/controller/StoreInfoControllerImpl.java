package com.skthvl.storefinder.controller;

import static com.skthvl.storefinder.util.NumberUtil.parseRadiusToMeters;

import com.skthvl.storefinder.mapper.StoreInfoMapper;
import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import com.skthvl.storefinder.model.response.PaginatedResponse;
import com.skthvl.storefinder.model.response.StoreOperationStatusResponse;
import com.skthvl.storefinder.service.StoreInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing store-related operations. Provides endpoints for retrieving store
 * information based on geographical location.
 */
@Slf4j
@RestController
public class StoreInfoControllerImpl implements StoreInfoController {
  private final StoreInfoService storeInfoService;
  private final StoreInfoMapper storeInfoMapper;

  /**
   * Constructs a new StoreController with the required StoreService.
   *
   * @param storeInfoService Service layer component for store information
   */
  public StoreInfoControllerImpl(
      final StoreInfoService storeInfoService, final StoreInfoMapper storeInfoMapper) {
    this.storeInfoService = storeInfoService;
    this.storeInfoMapper = storeInfoMapper;
  }

  @Override
  public ResponseEntity<PaginatedResponse<StoreDistanceDto>> getNearestStores(
      Double longitude, Double latitude, String radius, int page, int size) {

    final var radiusInMeter = parseRadiusToMeters(radius);
    log.debug("radiusInMeter: {}", radiusInMeter);

    final var storeDistanceDto =
        storeInfoService.findNearestStores(longitude, latitude, radiusInMeter, page, size);

    return ResponseEntity.ok().body(new PaginatedResponse<>(storeDistanceDto));
  }

  @Override
  public ResponseEntity<StoreOperationStatusResponse> getStoreOperationStatus(
      final String storeId) {

    final var storeOperation = storeInfoService.getOperationStatusBy(storeId);

    return ResponseEntity.ok().body(storeInfoMapper.toResponse(storeOperation));
  }
}
