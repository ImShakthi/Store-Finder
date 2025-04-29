package com.skthvl.storeapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.exception.type.StoreNotFoundException;
import com.skthvl.storeapi.mapper.StoreDtoMapper;
import com.skthvl.storeapi.model.dto.StoreDistanceDto;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.model.dto.StoreFilterCriteria;
import com.skthvl.storeapi.repository.StoreRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class StoreInfoServiceTest {

  @Mock private StoreRepository storeRepository;
  @Mock private StoreDtoMapper storeDtoMapper;
  @InjectMocks private StoreInfoService storeInfoService;

  @Test
  void findNearestStores_shouldReturnEmptyPageWhenNoStoresFound() {
    // Given
    final var longitude = 34.0544;
    final var latitude = -118.2439;
    final int page = 0;
    final int size = 5;
    final int radiusInMeter = 1000;
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<StoreDistanceDto> emptyPage = new PageImpl<>(Collections.emptyList());
    when(storeRepository.findNearestStoresBy(longitude, latitude, radiusInMeter, pageRequest))
        .thenReturn(emptyPage);

    // When
    final Page<StoreDistanceDto> result =
        storeInfoService.findNearestStores(longitude, latitude, radiusInMeter, page, size);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(storeRepository, times(1))
        .findNearestStoresBy(longitude, latitude, radiusInMeter, pageRequest);
  }

  @Test
  void findNearestStores_shouldReturnStoresWithinRadius() {
    // Given
    final var longitude = 34.0544;
    final var latitude = -118.2439;
    final int radiusInMeter = 1000;
    final int page = 0;
    final int size = 5;
    final PageRequest pageRequest = PageRequest.of(page, size);
    final StoreDistanceDto store1 = StoreDistanceDto.builder().build();
    final StoreDistanceDto store2 = StoreDistanceDto.builder().build();
    final List<StoreDistanceDto> stores = List.of(store1, store2);
    final Page<StoreDistanceDto> storePage = new PageImpl<>(stores);
    when(storeRepository.findNearestStoresBy(longitude, latitude, radiusInMeter, pageRequest))
        .thenReturn(storePage);

    // When
    final Page<StoreDistanceDto> result =
        storeInfoService.findNearestStores(longitude, latitude, radiusInMeter, page, size);

    // Then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(store1, result.getContent().get(0));
    assertEquals(store2, result.getContent().get(1));
    verify(storeRepository, times(1))
        .findNearestStoresBy(longitude, latitude, radiusInMeter, pageRequest);
  }

  @Test
  void getOperationStatusBy_shouldReturnStoreStatusWhenStoreExists() {
    // Given
    final String storeId = "store-12345";
    final var store = mock(Store.class);
    when(storeRepository.findByStoreId(storeId)).thenReturn(java.util.Optional.of(store));
    when(store.isStoreOpen()).thenReturn(true);
    when(store.getTodayOpen()).thenReturn(java.time.LocalTime.of(9, 0));
    when(store.getTodayClose()).thenReturn(java.time.LocalTime.of(21, 0));

    // When
    final var result = storeInfoService.getOperationStatusBy(storeId);

    // Then
    assertNotNull(result);
    assertEquals(storeId, result.getStoreId());
    assertEquals("OPEN", result.getStatus());
    assertEquals("09:00", result.getOpeningTime());
    assertEquals("21:00", result.getClosingTime());
    verify(storeRepository, times(1)).findByStoreId(storeId);
  }

  @Test
  void getOperationStatusBy_shouldThrowExceptionWhenStoreDoesNotExist() {
    // Given
    final String storeId = "store-12345";
    when(storeRepository.findByStoreId(storeId)).thenReturn(java.util.Optional.empty());

    // When & Then
    assertThrows(
        StoreNotFoundException.class, () -> storeInfoService.getOperationStatusBy(storeId));
    verify(storeRepository, times(1)).findByStoreId(storeId);
  }

  @Test
  void getStoreInfoBy_shouldReturnMappedStorePageWhenCriteriaMatch() {
    // Given
    final StoreFilterCriteria criteria =
        StoreFilterCriteria.builder()
            .city("Amsterdam")
            .openNow(true)
            .collectionPoint(false)
            .storeLocationType("SuperMarket")
            .page(0)
            .size(2)
            .build();

    final PageRequest pageRequest = PageRequest.of(criteria.page(), criteria.size());
    final Store store1 = Store.builder().build();
    final Store store2 = Store.builder().build();
    final List<Store> storeList = List.of(store1, store2);
    final Page<Store> storePage = new PageImpl<>(storeList, pageRequest, storeList.size());
    final StoreDto storeDto1 = StoreDto.builder().storeId("store-001").build();
    final StoreDto storeDto2 = StoreDto.builder().storeId("store-002").build();

    when(storeRepository.findStoreInfoBy(
            criteria.city(),
            criteria.openNow(),
            criteria.collectionPoint(),
            criteria.storeLocationType(),
            pageRequest))
        .thenReturn(storePage);
    when(storeDtoMapper.storeDto(store1)).thenReturn(storeDto1);
    when(storeDtoMapper.storeDto(store2)).thenReturn(storeDto2);

    // When
    final Page<StoreDto> result = storeInfoService.getStoreInfoBy(criteria);

    // Then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(storeDto1, result.getContent().get(0));
    assertEquals(storeDto2, result.getContent().get(1));
    verify(storeRepository, times(1))
        .findStoreInfoBy(
            criteria.city(),
            criteria.openNow(),
            criteria.collectionPoint(),
            criteria.storeLocationType(),
            pageRequest);
    verify(storeDtoMapper, times(2)).storeDto(any(Store.class));
  }

  @Test
  void getStoreInfoBy_shouldReturnEmptyPageWhenNoStoresMatchCriteria() {
    // Given
    final StoreFilterCriteria criteria =
        StoreFilterCriteria.builder()
            .city("Amsterdam")
            .openNow(false)
            .collectionPoint(true)
            .storeLocationType("SupermarktPuP")
            .page(0)
            .size(1)
            .build();

    final PageRequest pageRequest = PageRequest.of(criteria.page(), criteria.size());
    final Page<Store> emptyStorePage = new PageImpl<>(Collections.emptyList());

    when(storeRepository.findStoreInfoBy(
            criteria.city(),
            criteria.openNow(),
            criteria.collectionPoint(),
            criteria.storeLocationType(),
            pageRequest))
        .thenReturn(emptyStorePage);

    // When
    final Page<StoreDto> result = storeInfoService.getStoreInfoBy(criteria);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(storeRepository, times(1))
        .findStoreInfoBy(
            criteria.city(),
            criteria.openNow(),
            criteria.collectionPoint(),
            criteria.storeLocationType(),
            pageRequest);
    verify(storeDtoMapper, never()).storeDto(any(Store.class));
  }
}
