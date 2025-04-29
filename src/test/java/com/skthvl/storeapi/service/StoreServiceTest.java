package com.skthvl.storeapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.exception.type.StoreNotFoundException;
import com.skthvl.storeapi.mapper.StoreDtoMapper;
import com.skthvl.storeapi.mapper.StoreMapper;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.repository.StoreRepository;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StoreServiceTest {

  @Mock private StoreRepository storeRepository;

  @Mock private StoreMapper storeMapper;

  @Mock private StoreDtoMapper storeDtoMapper;

  @InjectMocks private StoreService storeService;

  public StoreServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void registerStore_successfulRegistration_returnsStoreDto() {
    StoreDto storeDto = new StoreDto();
    storeDto.setStoreId("1");
    storeDto.setSapStoreId("SAP1001");
    storeDto.setTodayOpen(LocalTime.of(9, 0));
    storeDto.setTodayClose(LocalTime.of(21, 0));

    Store store =
        Store.builder()
            .storeId("1")
            .sapStoreId("SAP1001")
            .todayOpen(LocalTime.of(9, 0))
            .todayClose(LocalTime.of(21, 0))
            .build();

    Store savedStore =
        Store.builder()
            .storeId("1")
            .sapStoreId("SAP1001")
            .todayOpen(LocalTime.of(9, 0))
            .todayClose(LocalTime.of(21, 0))
            .build();

    when(storeMapper.toStore(storeDto)).thenReturn(store);
    when(storeRepository.save(store)).thenReturn(savedStore);
    when(storeDtoMapper.storeDto(savedStore)).thenReturn(storeDto);

    StoreDto result = storeService.registerStore(storeDto);

    assertEquals("1", result.getStoreId());
    assertEquals("SAP1001", result.getSapStoreId());
    assertEquals(LocalTime.of(9, 0), result.getTodayOpen());
    assertEquals(LocalTime.of(21, 0), result.getTodayClose());

    verify(storeMapper).toStore(storeDto);
    verify(storeRepository).save(store);
    verify(storeDtoMapper).storeDto(savedStore);
  }

  @Test
  void deleteStore_successfulDeletion_doesNotThrowException() {
    String storeId = "1";

    when(storeRepository.existsByStoreId(storeId)).thenReturn(true);

    assertDoesNotThrow(() -> storeService.deleteStore(storeId));

    verify(storeRepository).existsByStoreId(storeId);
    verify(storeRepository).deleteByStoreId(storeId);
  }

  @Test
  void deleteStore_storeNotExist_throwsStoreNotFoundException() {
    String storeId = "1";

    when(storeRepository.existsByStoreId(storeId)).thenReturn(false);

    assertThrows(StoreNotFoundException.class, () -> storeService.deleteStore(storeId));

    verify(storeRepository).existsByStoreId(storeId);
    verify(storeRepository, never()).deleteByStoreId(storeId);
  }
}
