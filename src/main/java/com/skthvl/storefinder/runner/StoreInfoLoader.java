package com.skthvl.storefinder.runner;

import static java.util.Objects.requireNonNull;
import static org.apache.tomcat.util.net.openssl.ciphers.MessageDigest.MD5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.storefinder.entity.DataFileMigration;
import com.skthvl.storefinder.mapper.StoreMapper;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.model.dto.StoresDto;
import com.skthvl.storefinder.repository.DataFileMigrationRepository;
import com.skthvl.storefinder.repository.StoreRepository;
import com.skthvl.storefinder.util.FileUtil;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component responsible for loading store information from JSON files into the database. Handles
 * data migration tracking to prevent duplicate imports and provides file parsing capabilities.
 */
@Slf4j
@Component
public class StoreInfoLoader {
  private final ObjectMapper objectMapper;
  private final StoreMapper storeMapper;
  private final StoreRepository storeRepository;
  private final DataFileMigrationRepository dataFileMigrationRepository;

  /**
   * Constructs an instance of the StoreInfoLoader, initializing the required dependencies for
   * processing store information and managing data migration.
   *
   * @param objectMapper Utility for JSON serialization and deserialization tasks
   * @param storeMapper Mapper for converting StoreDto objects to Store entities and managing related dependencies
   * @param storeRepository Repository for managing Store entities and operations
   * @param dataFileMigrationRepository Repository for tracking and verifying data file migrations
   */
  public StoreInfoLoader(
      final ObjectMapper objectMapper,
      final StoreMapper storeMapper,
      final StoreRepository storeRepository,
      final DataFileMigrationRepository dataFileMigrationRepository) {
    this.objectMapper = objectMapper;
    this.storeMapper = storeMapper;
    this.storeRepository = storeRepository;
    this.dataFileMigrationRepository = dataFileMigrationRepository;
  }

  /**
   * Loads store data from a JSON file into the database. Checks for existing migrations using MD5
   * checksum to prevent duplicate imports.
   *
   * @param filePath path to the JSON file containing store data
   */
  @Transactional
  public void loadDataIntoDatabase(final String filePath) {
    try {
      final var fileChecksum = FileUtil.calculateChecksum(filePath, MD5.name());
      if (hasDataAlreadyMigrated(filePath, fileChecksum)) {
        log.info("data in '{}' already migrated into database", filePath);
        return;
      }

      final var stores =
          getStoresFromJsonFile(filePath).stream()
              .map(storeMapper::toStore)
              .filter(Objects::nonNull)
              .toList();
      log.info("loaded {} stores", stores.size());
      storeRepository.saveAll(stores);

      final var dataFileMigration =
          DataFileMigration.builder().filePath(filePath).fileChecksum(fileChecksum).build();
      dataFileMigrationRepository.save(dataFileMigration);
      log.debug("updated migration detail of {}", filePath);
    } catch (Exception e) {
      log.error("error in loading data into database :: {}", e.getMessage());
    }
  }

  /**
   * Checks if the file has already been migrated by comparing file path and checksum.
   *
   * @param filePath path to the file being checked
   * @param fileChecksum MD5 checksum of the file
   * @return true if file was already migrated, false otherwise
   */
  private boolean hasDataAlreadyMigrated(final String filePath, final String fileChecksum) {
    return dataFileMigrationRepository.existsByFilePathAndFileChecksum(filePath, fileChecksum);
  }

  /**
   * Parses store data from a JSON file into a list of StoreDto objects.
   *
   * @param fileName name of the JSON file to parse
   * @return List of parsed StoreDto objects, empty list if parsing fails
   */
  private List<StoreDto> getStoresFromJsonFile(final String fileName) {
    try {
      final var resource =
          requireNonNull(getClass().getClassLoader().getResource(fileName), "file not found");

      final var storesDto = objectMapper.readValue(resource, StoresDto.class);

      requireNonNull(storesDto, "storesDto cannot be null");

      return storesDto.getStores();
    } catch (Exception e) {
      log.error("error while parsing {} data:: {}", fileName, e.getMessage());
    }
    return List.of();
  }
}
