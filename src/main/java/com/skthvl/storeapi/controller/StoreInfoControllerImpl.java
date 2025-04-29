package com.skthvl.storeapi.controller;

import static com.skthvl.storeapi.util.NumberUtil.parseRadiusToMeters;

import com.skthvl.storeapi.mapper.StoreInfoMapper;
import com.skthvl.storeapi.model.dto.StoreDistanceDto;
import com.skthvl.storeapi.model.response.PaginatedResponse;
import com.skthvl.storeapi.model.response.StoreOperationStatusResponse;
import com.skthvl.storeapi.service.StoreInfoService;
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
