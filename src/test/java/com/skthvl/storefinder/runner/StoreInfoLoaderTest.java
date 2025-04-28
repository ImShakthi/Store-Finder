package com.skthvl.storefinder.runner;

import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.storefinder.entity.Address;
import com.skthvl.storefinder.entity.City;
import com.skthvl.storefinder.entity.Store;
import com.skthvl.storefinder.entity.StoreLocationType;
import com.skthvl.storefinder.mapper.StoreMapper;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.model.dto.StoresDto;
import com.skthvl.storefinder.repository.DataFileMigrationRepository;
import com.skthvl.storefinder.repository.StoreRepository;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StoreInfoLoaderTest {

  @Mock private ObjectMapper objectMapper;
  @Mock private GeometryFactory geometryFactory;
  @Mock private StoreRepository storeRepository;
  @Mock private StoreMapper storeMapper;
  @Mock private DataFileMigrationRepository dataFileMigrationRepository;

  private StoreInfoLoader storeInfoLoader;

  @BeforeEach
  void setUp() {
    storeInfoLoader =
        new StoreInfoLoader(objectMapper, storeMapper, storeRepository, dataFileMigrationRepository);
  }

  void loadDataIntoDatabase_ShouldLoadJsonDataIntoDatabase() {
    final var inputFilePath = "data/stores.json";
    final var storesDto = getStoresFromJsonFile(inputFilePath);
    final var store = toStore(storesDto.getFirst());
    final var stores = List.of(store);

    when(dataFileMigrationRepository.existsByFilePathAndFileChecksum(
            inputFilePath, "8304cd2c909a5ee6ab3604bc78283578"))
        .thenReturn(false);
    when(storeMapper.toStore(storesDto.getFirst())).thenReturn(store);
    when(storeRepository.saveAll(stores)).thenReturn(stores);

    storeInfoLoader.loadDataIntoDatabase(inputFilePath);

    verify(storeRepository).saveAll(stores);
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

    Point location =
        new Point(
            new CoordinateArraySequence(new Coordinate[] {new Coordinate()}), geometryFactory);

    return Store.builder()
        .address(address)
        .city(city)
        .storeLocationType(locType)
        .storeId(storeDto.getStoreId())
        .sapStoreId(storeDto.getSapStoreId())
        .complexNumber(storeDto.getComplexNumber())
        .location(location)
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
