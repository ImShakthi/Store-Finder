package com.skthvl.storeapi.mapper;

import static java.util.Objects.isNull;

import com.skthvl.storeapi.entity.Address;
import com.skthvl.storeapi.entity.City;
import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.entity.StoreLocationType;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.repository.AddressRepository;
import com.skthvl.storeapi.repository.CityRepository;
import com.skthvl.storeapi.repository.StoreLocationTypeRepository;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Maps StoreDto objects to Store entities while managing caching of City, Address, and
 * StoreLocationType entities to improve performance during bulk operations.
 */
@Slf4j
@Component
public class StoreMapper {
  private final ConcurrentHashMap<String, City> cityCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Address> addressCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, StoreLocationType> locTypeCache =
      new ConcurrentHashMap<>();

  /** Repository for managing City entities. */
  private final CityRepository cityRepository;

  /** Repository for managing Address entities. */
  private final AddressRepository addressRepository;

  /** Repository for managing StoreLocationType entities. */
  private final StoreLocationTypeRepository storeLocationTypeRepository;

  /** Factory for creating geometric objects with SRID 4326. */
  private final GeometryFactory geometryFactory;

  /**
   * Constructs a new instance of the StoreMapper class and initializes the required repositories
   * and resources.
   *
   * @param cityRepository Repository for managing City entities.
   * @param addressRepository Repository for managing Address entities.
   * @param storeLocationTypeRepository Repository for managing StoreLocationType entities.
   */
  public StoreMapper(
      final CityRepository cityRepository,
      final AddressRepository addressRepository,
      final StoreLocationTypeRepository storeLocationTypeRepository) {
    this.cityRepository = cityRepository;
    this.addressRepository = addressRepository;
    this.storeLocationTypeRepository = storeLocationTypeRepository;

    this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
  }

  /**
   * Converts a StoreDto to a Store entity, handling the creation and caching of related entities.
   *
   * @param storeDto The DTO containing store information
   * @return A Store entity with all properties mapped from the DTO
   */
  @Transactional
  public Store toStore(final StoreDto storeDto) {
    final var address = getAddress(storeDto);
    log.debug("address is {}", address);
    final var city = getCity(storeDto);
    log.debug("city is {}", city);
    final var locType = getLocType(storeDto);
    log.debug("location type is {}", locType);

    return Store.builder()
        .address(address)
        .city(city)
        .storeLocationType(locType)
        .storeId(getStoreId(storeDto))
        .sapStoreId(storeDto.getSapStoreId())
        .complexNumber(storeDto.getComplexNumber())
        .location(getLocation(storeDto))
        .showWarningMessage(storeDto.isShowWarningMessage())
        .collectionPoint(storeDto.isCollectionPoint())
        .todayClose(storeDto.getTodayClose())
        .todayOpen(storeDto.getTodayOpen())
        .build();
  }

  private Address getAddress(final StoreDto storeDto) {
    final var key = generateAddressKey(storeDto);
    return addressCache.computeIfAbsent(
        key, s -> addressRepository.save(populateAddress(storeDto)));
  }

  private City getCity(final StoreDto storeDto) {
    return cityCache.computeIfAbsent(
        storeDto.getCity(), s -> cityRepository.save(populateCity(storeDto)));
  }

  private StoreLocationType getLocType(final StoreDto storeDto) {
    return locTypeCache.computeIfAbsent(
        storeDto.getLocationType(),
        s -> storeLocationTypeRepository.save(populateLocType(storeDto)));
  }

  private Address populateAddress(final StoreDto storeDto) {
    return Address.builder()
        .addressName(storeDto.getAddressName())
        .street(storeDto.getStreet())
        .street2(storeDto.getStreet2())
        .street3(storeDto.getStreet3())
        .postalCode(storeDto.getPostalCode())
        .build();
  }

  private City populateCity(final StoreDto storeDto) {
    return City.builder().name(storeDto.getCity()).build();
  }

  private StoreLocationType populateLocType(final StoreDto storeDto) {
    return StoreLocationType.builder().name(storeDto.getLocationType()).build();
  }

  private String generateAddressKey(final StoreDto storeDto) {
    return String.format(
        "%s-%s-%s-%s-%s-%s",
        storeDto.getCity(),
        storeDto.getAddressName(),
        storeDto.getStreet(),
        storeDto.getStreet2(),
        storeDto.getStreet3(),
        storeDto.getPostalCode());
  }

  private Point getLocation(final StoreDto storeDto) {
    return geometryFactory.createPoint(
        new Coordinate(storeDto.getLongitude(), storeDto.getLatitude()));
  }

  private String getStoreId(final StoreDto storeDto) {
    return isNull(storeDto.getStoreId()) ? UUID.randomUUID().toString() : storeDto.getStoreId();
  }
}
