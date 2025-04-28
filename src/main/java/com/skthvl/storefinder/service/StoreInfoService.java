package com.skthvl.storefinder.service;

import com.skthvl.storefinder.exception.type.StoreNotFoundException;
import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import com.skthvl.storefinder.model.dto.StoreOperationStatusDto;
import com.skthvl.storefinder.repository.StoreRepository;
import java.time.format.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreInfoService {
  private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  private final StoreRepository storeRepository;

  public StoreInfoService(final StoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }

  /**
   * Finds stores nearest to the specified geographical coordinates.
   *
   * @param longitude Geographical longitude coordinate
   * @param latitude Geographical latitude coordinate
   * @param radiusInMeter
   * @param page Zero-based page index
   * @param size The size of the page to be returned
   * @return Page of StoreDistanceDto containing nearest stores with their distances
   */
  @Transactional(readOnly = true)
  public Page<StoreDistanceDto> findNearestStores(
      final Double longitude,
      final Double latitude,
      int radiusInMeter,
      final int page,
      final int size) {

    return storeRepository.findNearestStoresBy(
        longitude, latitude, radiusInMeter, PageRequest.of(page, size));
  }

  @Transactional(readOnly = true)
  public StoreOperationStatusDto getOperationStatusBy(final String storeId) {
    final var store =
        storeRepository.findByStoreId(storeId).orElseThrow(StoreNotFoundException::new);

    final var operationStatus = store.isStoreOpen() ? "OPEN" : "CLOSED";

    return StoreOperationStatusDto.builder()
        .storeId(storeId)
        .status(operationStatus)
        .openingTime(store.getTodayOpen().format(timeFormatter))
        .closingTime(store.getTodayClose().format(timeFormatter))
        .build();
  }
}
