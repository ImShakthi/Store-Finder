package com.skthvl.storefinder.service;

import com.skthvl.storefinder.exception.type.StoreNotFoundException;
import com.skthvl.storefinder.mapper.StoreDtoMapper;
import com.skthvl.storefinder.mapper.StoreMapper;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
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

  public StoreDto registerStore(final StoreDto storeDto) {
    final var store = storeRepository.save(storeMapper.toStore(storeDto));
    log.info("store with id {} is registered", store.getStoreId());
    return storeDtoMapper.storeDto(store);
  }

  @Transactional
  public StoreDto modifyStore(final StoreDto storeDto, final String storeId) {
    final var store =
        storeRepository.findByStoreId(storeId).orElseThrow(StoreNotFoundException::new);

    // TODO: add logic to modify store details

    return storeDtoMapper.storeDto(store);
  }

  @Transactional
  public void deleteStore(final String storeId) {
    if (!storeRepository.existsByStoreId(storeId)) {
      throw new StoreNotFoundException();
    }

    storeRepository.deleteByStoreId(storeId);
    log.info("store with id {} is deleted", storeId);
  }
}
