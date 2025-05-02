package com.skthvl.storeapi.service;

import com.skthvl.storeapi.exception.type.StoreNotFoundException;
import com.skthvl.storeapi.mapper.StoreDtoMapper;
import com.skthvl.storeapi.mapper.StoreMapper;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing store operations including registration, modification, and deletion.
 * Handles CRUD operations and business logic for store management.
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
   * Registers a new store in the system.
   *
   * @param storeDto Store information to be registered
   * @return StoreDto containing the registered store details
   */
  public StoreDto registerStore(final StoreDto storeDto) {
    final var store = storeRepository.save(storeMapper.toStore(storeDto));
    log.info("store with id {} is registered", store.getStoreId());
    return storeDtoMapper.storeDto(store);
  }

  /**
   * Modifies an existing store's information.
   *
   * @param storeDto Updated store information
   * @param storeId Identifier of the store to be modified
   * @return StoreDto containing the modified store details
   * @throws StoreNotFoundException if store with given ID is not found
   */
  @Transactional
  public StoreDto modifyStore(final StoreDto storeDto, final String storeId) {
    final var store =
        storeRepository.findByStoreId(storeId).orElseThrow(StoreNotFoundException::new);

    final var updatedStore = storeMapper.modifyFrom(store, storeDto);

    return storeDtoMapper.storeDto(storeRepository.save(updatedStore));
  }

  /**
   * Deletes a store from the system.
   *
   * @param storeId Identifier of the store to be deleted
   * @throws StoreNotFoundException if store with given ID is not found
   */
  @Transactional
  public void deleteStore(final String storeId) {
    if (!storeRepository.existsByStoreId(storeId)) {
      throw new StoreNotFoundException();
    }

    storeRepository.deleteByStoreId(storeId);
    log.info("store with id {} is deleted", storeId);
  }

  /**
   * Get a store information from the system by its unique identifier.
   *
   * @param storeId Identifier of the store to be fetched
   * @throws StoreNotFoundException if store with given ID is not found
   */
  @Transactional(readOnly = true)
  public StoreDto getStore(final String storeId) {
    final var store =
        storeRepository.findByStoreId(storeId).orElseThrow(StoreNotFoundException::new);

    return storeDtoMapper.storeDto(store);
  }
}
