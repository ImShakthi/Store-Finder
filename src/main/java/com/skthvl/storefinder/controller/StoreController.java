package com.skthvl.storefinder.controller;

import com.skthvl.storefinder.mapper.StoreDtoMapper;
import com.skthvl.storefinder.model.dto.StoreFilterCriteria;
import com.skthvl.storefinder.model.request.CreateStoreRequest;
import com.skthvl.storefinder.model.response.MessageResponse;
import com.skthvl.storefinder.model.response.PaginatedResponse;
import com.skthvl.storefinder.model.response.StoreResponse;
import com.skthvl.storefinder.service.StoreInfoService;
import com.skthvl.storefinder.service.StoreService;
import com.skthvl.storefinder.util.PageUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing store-related operations. Provides endpoints for retrieving store
 * information based on geographical location.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {
  private final StoreService storeService;
  private final StoreDtoMapper storeDtoMapper;
  private final StoreInfoService storeInfoService;

  /**
   * Constructs a new StoreController with the required StoreService.
   *
   * @param storeService Service layer component for store operations
   * @param storeDtoMapper Mapper for converting Store request to StoreDto objects
   */
  public StoreController(
      final StoreService storeService,
      final StoreDtoMapper storeDtoMapper,
      StoreInfoService storeInfoService) {
    this.storeService = storeService;
    this.storeDtoMapper = storeDtoMapper;
    this.storeInfoService = storeInfoService;
  }

  /**
   * Creates a new store with the provided details.
   *
   * @param createStoreRequest The store details for creation
   * @return ResponseEntity containing the created store details
   */
  @PostMapping
  public ResponseEntity<StoreResponse> createStore(
      @Valid @RequestBody final CreateStoreRequest createStoreRequest) {

    final var storeDto = storeService.registerStore(storeDtoMapper.storeDto(createStoreRequest));

    return ResponseEntity.status(HttpStatus.CREATED).body(storeDtoMapper.toStoreResponse(storeDto));
  }

  /**
   * Retrieves a paginated list of stores based on the specified filtering criteria.
   *
   * @param city The city where the store is located. Optional parameter.
   * @param openNow Indicates if only currently open stores should be retrieved. Optional parameter.
   * @param collectionPoint Indicates if only stores functioning as collection points should be
   *     included. Optional parameter.
   * @param storeLocationType The type of store location (e.g., urban, rural). Optional parameter.
   * @param page The page number to retrieve for the paginated response. Default value is 0.
   * @param size The number of items per page for the paginated response. Default value is 5.
   * @return ResponseEntity containing a paginated response with a list of stores that match the
   *     filtering criteria.
   */
  @GetMapping
  public ResponseEntity<PaginatedResponse<List<StoreResponse>>> getStores(
      @RequestParam(required = false) final String city,
      @RequestParam(required = false) final Boolean openNow,
      @RequestParam(required = false) final Boolean collectionPoint,
      @RequestParam(required = false) final String storeLocationType,
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "5") final int size) {

    final var filters =
        StoreFilterCriteria.builder()
            .city(city)
            .openNow(openNow)
            .collectionPoint(collectionPoint)
            .storeLocationType(storeLocationType)
            .page(page)
            .size(size)
            .build();

    final var storeDtoPage = storeInfoService.getStoreInfoBy(filters);

    return ResponseEntity.ok(
        new PaginatedResponse(PageUtils.mapPage(storeDtoPage, storeDtoMapper::toStoreResponse)));
  }

  /**
   * Updates an existing store with the provided details.
   *
   * @param createStoreRequest The updated store details
   * @param storeId The ID of the store to modify
   * @return ResponseEntity containing the modified store details
   */
  @PutMapping("/{storeId}")
  public ResponseEntity<StoreResponse> modifyStore(
      @Valid @RequestBody final CreateStoreRequest createStoreRequest,
      @PathVariable("storeId") final String storeId) {

    final var storeDto =
        storeService.modifyStore(storeDtoMapper.storeDto(createStoreRequest), storeId);

    return ResponseEntity.ok().body(storeDtoMapper.toStoreResponse(storeDto));
  }

  /**
   * Deletes a store with the specified ID.
   *
   * @param storeId The ID of the store to delete
   * @return ResponseEntity containing confirmation message
   */
  @DeleteMapping("/{storeId}")
  public ResponseEntity<MessageResponse> deleteStore(
      @PathVariable("storeId") final String storeId) {
    storeService.deleteStore(storeId);

    return ResponseEntity.ok().body(new MessageResponse("store details is modified."));
  }
}
