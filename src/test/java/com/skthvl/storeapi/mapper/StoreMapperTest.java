package com.skthvl.storeapi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.storeapi.entity.Address;
import com.skthvl.storeapi.entity.City;
import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.entity.StoreLocationType;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.repository.AddressRepository;
import com.skthvl.storeapi.repository.CityRepository;
import com.skthvl.storeapi.repository.StoreLocationTypeRepository;
import java.math.BigInteger;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreMapperTest {

  @Mock private CityRepository cityRepository;
  @Mock private AddressRepository addressRepository;
  @Mock private StoreLocationTypeRepository storeLocationTypeRepository;

  private StoreMapper storeMapper;

  @BeforeEach
  void setup() {
    storeMapper = new StoreMapper(cityRepository, addressRepository, storeLocationTypeRepository);
  }

  @Test
  void shouldMapStoreDtoToStoreEntityCorrectly_andCacheEntities() {
    // Given
    StoreDto dto = createSampleStoreDto();

    City city = City.builder().id(BigInteger.ONE).name(dto.getCity()).build();
    Address address =
        Address.builder().id(BigInteger.ONE).addressName(dto.getAddressName()).build();
    StoreLocationType locationType =
        StoreLocationType.builder().id(BigInteger.ONE).name(dto.getLocationType()).build();

    when(cityRepository.save(any(City.class))).thenReturn(city);
    when(addressRepository.save(any(Address.class))).thenReturn(address);
    when(storeLocationTypeRepository.save(any(StoreLocationType.class))).thenReturn(locationType);

    // When
    Store store = storeMapper.toStore(dto);

    // Then
    assertNotNull(store);
    assertEquals(dto.getAddressName(), store.getAddress().getAddressName());
    assertEquals(dto.getCity(), store.getCity().getName());
    assertEquals(dto.getLocationType(), store.getStoreLocationType().getName());

    assertEquals(dto.getLatitude(), store.getLocation().getY());
    assertEquals(dto.getLongitude(), store.getLocation().getX());

    // Should call save only once per entity (cached)
    storeMapper.toStore(dto);

    verify(cityRepository, times(1)).save(any());
    verify(addressRepository, times(1)).save(any());
    verify(storeLocationTypeRepository, times(1)).save(any());
  }

  private StoreDto createSampleStoreDto() {
    return StoreDto.builder()
        .storeId(null)
        .sapStoreId("1234")
        .complexNumber("C-100")
        .longitude(4.88456)
        .latitude(52.3676)
        .showWarningMessage(true)
        .collectionPoint(true)
        .todayOpen(LocalTime.of(8, 0))
        .todayClose(LocalTime.of(20, 0))
        .city("Amsterdam")
        .addressName("Jumbo Amsterdam")
        .street("Kalverstraat")
        .street2("Block B")
        .street3("Floor 1")
        .postalCode("1012NX")
        .locationType("Supermarkt")
        .build();
  }
}
