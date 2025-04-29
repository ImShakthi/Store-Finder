package com.skthvl.storeapi.service;

import static com.skthvl.storeapi.util.PageUtils.mapPage;

import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.exception.type.StoreNotFoundException;
import com.skthvl.storeapi.mapper.StoreDtoMapper;
import com.skthvl.storeapi.model.dto.StoreDistanceDto;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.model.dto.StoreFilterCriteria;
import com.skthvl.storeapi.model.dto.StoreOperationStatusDto;
import com.skthvl.storeapi.repository.StoreRepository;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for handling store-related operations including finding nearest stores
 * and checking store operation status.
 */
@Slf4j
@Service
public class StoreInfoService {
  private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  private final StoreRepository storeRepository;
  private final StoreDtoMapper storeDtoMapper;

  /**
   * Constructs a StoreInfoService with required repository dependency.
   *
   * @param storeRepository Repository for accessing store data
   */
  public StoreInfoService(
      final StoreRepository storeRepository, final StoreDtoMapper storeDtoMapper) {
    this.storeRepository = storeRepository;
    this.storeDtoMapper = storeDtoMapper;
  }

  /**
   * Finds stores nearest to the specified geographical coordinates.
   *
   * @param longitude Geographical longitude coordinate
   * @param latitude Geographical latitude coordinate
   * @param radiusInMeter Radius is meter
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

  /**
   * Retrieves the current operation status of a store.
   *
   * @param storeId Unique identifier of the store
   * @return StoreOperationStatusDto containing store's operation status and timing details
   * @throws StoreNotFoundException if store with given ID is not found
   */
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

  /**
   * Retrieves store information based on the specified filtering criteria.
   *
   * @param criteria the filter criteria containing city, open status, collection point information,
   *     store location type, and paging parameters
   * @return a page of StoreDto containing the store information matching the given criteria
   */
  @Transactional(readOnly = true)
  public Page<StoreDto> getStoreInfoBy(final StoreFilterCriteria criteria) {

    final Page<Store> storePage = storeRepository.findStoreInfoBy(
            criteria.city(),
            criteria.openNow(),
            criteria.collectionPoint(),
            criteria.storeLocationType(),
            PageRequest.of(criteria.page(), criteria.size()));
    log.debug("found {} stores matching criteria", storePage.getTotalElements());
    return mapPage(storePage, storeDtoMapper::storeDto);
  }
}
