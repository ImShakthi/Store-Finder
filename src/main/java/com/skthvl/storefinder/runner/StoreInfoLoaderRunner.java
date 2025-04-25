package com.skthvl.storefinder.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreInfoLoaderRunner implements ApplicationRunner {

  private final StoreInfoLoader storeInfoLoader;

  @Override
  public void run(final ApplicationArguments args) {
    storeInfoLoader.loadDataIntoDatabase("db/data/stores.json");
  }
}
