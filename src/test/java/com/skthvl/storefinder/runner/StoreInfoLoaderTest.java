package com.skthvl.storefinder.runner;

import com.skthvl.storefinder.mapper.StoreMapper;
import com.skthvl.storefinder.repository.DataFileMigrationRepository;
import com.skthvl.storefinder.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreInfoLoaderTest {

  @Mock private StoreRepository storeRepository;
  @Mock private StoreMapper storeMapper;
  @Mock private DataFileMigrationRepository dataFileMigrationRepository;

  private StoreInfoLoader storeInfoLoader;

  @BeforeEach
  void setUp() {
    storeInfoLoader =
        new StoreInfoLoader(storeMapper, storeRepository, dataFileMigrationRepository);
  }

  @Test
  void loadDataIntoDatabase_ShouldLoadJsonDataIntoDatabase() {

    storeInfoLoader.loadDataIntoDatabase("data/stores.json");
  }
}
