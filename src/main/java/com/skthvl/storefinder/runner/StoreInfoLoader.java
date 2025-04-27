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
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class StoreInfoLoader {

  private final StoreMapper storeMapper;
  private final StoreRepository storeRepository;
  private final DataFileMigrationRepository dataFileMigrationRepository;

  public StoreInfoLoader(
      final StoreMapper storeMapper,
      final StoreRepository storeRepository,
      final DataFileMigrationRepository dataFileMigrationRepository) {
    this.storeMapper = storeMapper;
    this.storeRepository = storeRepository;
    this.dataFileMigrationRepository = dataFileMigrationRepository;
  }

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
              .peek(store -> log.info(">>> store ={}", store))
              .toList();

      log.debug("Found {} stores in file {}", stores.size(), filePath);
      storeRepository.saveAll(stores);

      final var dataFileMigration =
          DataFileMigration.builder().filePath(filePath).fileChecksum(fileChecksum).build();
      dataFileMigrationRepository.save(dataFileMigration);
      log.debug("updated migration detail of {}", filePath);
    } catch (Exception e) {
      log.error("error in loading data into database :: {}", e.getMessage());
    }
  }

  private boolean hasDataAlreadyMigrated(final String filePath, final String fileChecksum) {
    return dataFileMigrationRepository.existsByFilePathAndFileChecksum(filePath, fileChecksum);
  }

  private List<StoreDto> getStoresFromJsonFile(final String fileName) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();

      final var fileUri =
          requireNonNull(getClass().getClassLoader().getResource(fileName), "file not found")
              .toURI();

      // convert a JSON string to a Book object
      final var storesDto = objectMapper.readValue(Paths.get(fileUri).toFile(), StoresDto.class);

      requireNonNull(storesDto, "storesDto cannot be null");

      return storesDto.getStores();
    } catch (Exception e) {
      log.error("error while parsing {} data:: {}", fileName, e.getMessage());
    }
    return List.of();
  }
}
