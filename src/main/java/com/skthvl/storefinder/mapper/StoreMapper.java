package com.skthvl.storefinder.mapper;

import static java.lang.Double.parseDouble;

import com.skthvl.storefinder.entity.Address;
import com.skthvl.storefinder.entity.City;
import com.skthvl.storefinder.entity.Store;
import com.skthvl.storefinder.entity.StoreLocationType;
import com.skthvl.storefinder.exception.InvalidTimeFormatException;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.repository.AddressRepository;
import com.skthvl.storefinder.repository.CityRepository;
import com.skthvl.storefinder.repository.StoreLocationTypeRepository;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StoreMapper {
  private final ConcurrentHashMap<String, City> cityCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Address> addressCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, StoreLocationType> locTypeCache =
      new ConcurrentHashMap<>();

  private final CityRepository cityRepository;
  private final AddressRepository addressRepository;
  private final StoreLocationTypeRepository storeLocationTypeRepository;
  private final GeometryFactory geometryFactory;

  public StoreMapper(
      final CityRepository cityRepository,
      final AddressRepository addressRepository,
      final StoreLocationTypeRepository storeLocationTypeRepository) {
    this.cityRepository = cityRepository;
    this.addressRepository = addressRepository;
    this.storeLocationTypeRepository = storeLocationTypeRepository;

    this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
  }

  public Store toStore(final StoreDto storeDto) {

    final var address = getAddress(storeDto);
    log.info("address is {}", address);
    final var city = getCity(storeDto);
    log.info("city is {}", city);
    final var locType = getLocType(storeDto);
    log.info("location type is {}", locType);

    return Store.builder()
        .address(address)
        .city(city)
        .storeLocationType(locType)
        .uuid(storeDto.getUuid())
        .sapStoreId(storeDto.getSapStoreID())
        .complexNumber(storeDto.getComplexNumber())
        .location(getLocation(storeDto))
        .showWarningMessage(storeDto.isShowWarningMessage())
        .collectionPoint(storeDto.isCollectionPoint())
        .todayClose(parseTime(storeDto.getTodayClose()))
        .todayOpen(parseTime(storeDto.getTodayOpen()))
        .build();
  }

  private Address getAddress(final StoreDto storeDto) {
    final var key = generateAddressKey(storeDto);
    log.info("key for address is {}", key);
    return addressCache.computeIfAbsent(
        key, s -> addressRepository.save(populateAddress(storeDto)));
  }

  private City getCity(final StoreDto storeDto) {
    log.info("key for city is {}", storeDto.getCity());
    return cityCache.computeIfAbsent(
        storeDto.getCity(), s -> cityRepository.save(populateCity(storeDto)));
  }

  private StoreLocationType getLocType(final StoreDto storeDto) {
    log.info("key for location type is {}", storeDto.getLocationType());
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

  private LocalTime parseTime(final String time) {
    if ("GESLOTEN".equalsIgnoreCase(time)) {
      return null;
    }
    final var hourAndMinutes = Arrays.stream(time.split(":")).map(Integer::parseInt).toList();
    if (hourAndMinutes.size() != 2) {
      throw new InvalidTimeFormatException();
    }
    return LocalTime.of(hourAndMinutes.getFirst(), hourAndMinutes.getLast());
  }

  public String generateAddressKey(final StoreDto storeDto) {
    return String.format(
        "%s-%s-%s-%s",
        storeDto.getCity(),
        storeDto.getAddressName(),
        storeDto.getStreet(),
        storeDto.getPostalCode());
  }

  public Point getLocation(final StoreDto storeDto) {
    return geometryFactory.createPoint(
        new Coordinate(parseDouble(storeDto.getLongitude()), parseDouble(storeDto.getLatitude())));
  }
}
