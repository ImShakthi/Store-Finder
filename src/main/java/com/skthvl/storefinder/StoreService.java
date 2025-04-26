package com.skthvl.storefinder;

import com.skthvl.storefinder.repository.StoreRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
  private final StoreRepository storeRepository;

  public StoreService(final StoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }
}
