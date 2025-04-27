package com.skthvl.storefinder.service;

import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import com.skthvl.storefinder.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service class for managing store-related operations and queries.
 * Provides functionality for finding nearest stores based on geographical coordinates.
 */
@Service
public class StoreService {
  private final StoreRepository storeRepository;

  /**
   * Constructs a new StoreService with the required repository.
   *
   * @param storeRepository Repository for store data access operations
   */
  public StoreService(final StoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }

  /**
   * Finds stores nearest to the specified geographical coordinates.
   *
   * @param longitude Geographical longitude coordinate
   * @param latitude Geographical latitude coordinate
   * @param page Zero-based page index
   * @param size The size of the page to be returned
   * @return Page of StoreDistanceDto containing nearest stores with their distances
   */
  public Page<StoreDistanceDto> findNearestStores(
      final Double longitude, final Double latitude, final int page, final int size) {
    return storeRepository.findNearestStoresBy(longitude, latitude, PageRequest.of(page, size));
  }
}
