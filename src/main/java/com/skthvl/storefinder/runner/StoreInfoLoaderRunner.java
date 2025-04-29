package com.skthvl.storefinder.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Application runner responsible for loading store information data into the database during
 * application startup. This component reads store data from a JSON file specified in the
 * application properties and initializes the database with store information.
 */
@Component
@RequiredArgsConstructor
public class StoreInfoLoaderRunner implements ApplicationRunner {

  /**
   * File path to the JSON file containing store information, configured in application properties.
   */
  @Value("${store-api.data.json.stores-info}")
  private String storesInfoFilePath;

  /** Service component responsible for loading store data into the database. */
  private final StoreInfoLoader storeInfoLoader;

  /**
   * Executes the data loading process when the application starts.
   *
   * @param args Application startup arguments
   */
  @Override
  public void run(final ApplicationArguments args) {
    storeInfoLoader.loadDataIntoDatabase(storesInfoFilePath);
  }
}
