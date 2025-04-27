package com.skthvl.storefinder;

import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import com.skthvl.storefinder.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
  private final StoreRepository storeRepository;

  public StoreService(final StoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }

  public Page<StoreDistanceDto> findNearestStores(
      final Double longitude, final Double latitude, final int page, final int size) {
    return storeRepository.findNearestStoresBy(longitude, latitude, PageRequest.of(page, size));
  }
}
