package com.skthvl.storefinder.runner;

import com.skthvl.storefinder.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreInfoLoaderTest {

  @Mock StoreRepository storeRepository;

  private StoreInfoLoader storeInfoLoader;

  @BeforeEach
  void setUp() {
    storeInfoLoader = new StoreInfoLoader(storeRepository);
  }

  @Test
  void loadDataIntoDatabase_ShouldLoadJsonDataIntoDatabase() {

    storeInfoLoader.loadDataIntoDatabase("data/stores.json");
  }
}
