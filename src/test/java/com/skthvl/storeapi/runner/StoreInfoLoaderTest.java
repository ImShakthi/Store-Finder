package com.skthvl.storeapi.runner;

import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.storeapi.entity.Address;
import com.skthvl.storeapi.entity.City;
import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.entity.StoreLocationType;
import com.skthvl.storeapi.mapper.StoreMapper;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.model.dto.StoresDto;
import com.skthvl.storeapi.repository.DataFileMigrationRepository;
import com.skthvl.storeapi.repository.StoreRepository;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StoreInfoLoaderTest {

  @Mock private Point point;
  @Mock private StoreRepository storeRepository;
  @Mock private StoreMapper storeMapper;
  @Mock private DataFileMigrationRepository dataFileMigrationRepository;

  private StoreInfoLoader storeInfoLoader;

  @BeforeEach
  void setUp() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    storeInfoLoader =
        new StoreInfoLoader(objectMapper, storeMapper, storeRepository, dataFileMigrationRepository);
  }

//  @Test
  void loadDataIntoDatabase_ShouldLoadJsonDataIntoDatabase() {
    final var inputFilePath = "data/stores.json";
    final var storesDto = getStoresFromJsonFile(inputFilePath);
    final var store = toStore(storesDto.getFirst());
    final var stores = List.of(store);

    when(dataFileMigrationRepository.existsByFilePathAndFileChecksum(
            inputFilePath, "8304cd2c909a5ee6ab3604bc78283578"))
        .thenReturn(false);
//    when(storeMapper.toStore(eq(storesDto.getFirst()))).thenReturn(eq(store));
    doReturn(store).when(storeMapper).toStore(storesDto.getFirst());
    when(storeRepository.saveAll(stores)).thenReturn(stores);

    storeInfoLoader.loadDataIntoDatabase(inputFilePath);

    //    verify(storeRepository).saveAll(any());
  }

  private Store toStore(final StoreDto storeDto) {
    final var address =
        Address.builder()
            .addressName(storeDto.getAddressName())
            .street(storeDto.getStreet())
            .street2(storeDto.getStreet2())
            .street3(storeDto.getStreet3())
            .postalCode(storeDto.getPostalCode())
            .build();
    final var city = City.builder().name(storeDto.getCity()).build();
    final var locType = StoreLocationType.builder().name(storeDto.getLocationType()).build();

    return Store.builder()
        .address(address)
        .city(city)
        .storeLocationType(locType)
        .storeId(storeDto.getStoreId())
        .sapStoreId(storeDto.getSapStoreId())
        .complexNumber(storeDto.getComplexNumber())
        .location(point)
        .showWarningMessage(storeDto.isShowWarningMessage())
        .collectionPoint(storeDto.isCollectionPoint())
        .todayClose(storeDto.getTodayClose())
        .todayOpen(storeDto.getTodayOpen())
        .build();
  }

  private List<StoreDto> getStoresFromJsonFile(final String fileName) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();

      final var fileUri =
          requireNonNull(getClass().getClassLoader().getResource(fileName), "file not found")
              .toURI();

      final var storesDto = objectMapper.readValue(Paths.get(fileUri).toFile(), StoresDto.class);
      requireNonNull(storesDto, "storesDto cannot be null");

      return storesDto.getStores();
    } catch (Exception e) {
      e.printStackTrace();
      log.error("error while parsing {} data:: {}", fileName, e.getMessage());
    }
    return List.of();
  }
}
