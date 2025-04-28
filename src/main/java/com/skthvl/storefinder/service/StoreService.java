package com.skthvl.storefinder.service;

import com.skthvl.storefinder.exception.type.StoreNotFoundException;
import com.skthvl.storefinder.mapper.StoreDtoMapper;
import com.skthvl.storefinder.mapper.StoreMapper;
import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing store-related operations and queries. Provides functionality for
 * finding nearest stores based on geographical coordinates.
 */
@Slf4j
@Service
public class StoreService {
  private final StoreRepository storeRepository;
  private final StoreMapper storeMapper;
  private final StoreDtoMapper storeDtoMapper;

  /**
   * Constructor for the StoreService class.
   *
   * @param storeRepository Repository for managing store entities with spatial search capabilities
   * @param storeMapper Mapper for converting store-related data between different layers
   * @param storeDtoMapper Mapper for converting store DTO objects
   */
  public StoreService(
      final StoreRepository storeRepository,
      final StoreMapper storeMapper,
      final StoreDtoMapper storeDtoMapper) {
    this.storeRepository = storeRepository;
    this.storeMapper = storeMapper;
    this.storeDtoMapper = storeDtoMapper;
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
  @Transactional
  public Page<StoreDistanceDto> findNearestStores(
      final Double longitude, final Double latitude, final int page, final int size) {
    return storeRepository.findNearestStoresBy(longitude, latitude, PageRequest.of(page, size));
  }

  public StoreDto registerStore(final StoreDto storeDto) {
    final var store = storeRepository.save(storeMapper.toStore(storeDto));
    log.info("store with id {} is registered", store.getUuid());
    return storeDtoMapper.storeDto(store);
  }

  @Transactional
  public StoreDto modifyStore(final StoreDto storeDto, final String storeId) {
    final var store = storeRepository.findByUuid(storeId).orElseThrow(StoreNotFoundException::new);

    // TODO: add logic to modify store details

    return storeDtoMapper.storeDto(store);
  }

  @Transactional
  public void deleteStore(final String storeId) {
//    storeRepository
//        .findByUuid(storeId)
//        .map(
//            store -> {
//              log.info("store {} to be deleted", store.getAddress());
//              storeRepository.delete(store);
//              log.info("store with id {} is deleted", storeId);
//              return true;
//            })
//        .orElseThrow(StoreNotFoundException::new);
  }
}
