package com.skthvl.storefinder.mapper;

import static com.skthvl.storefinder.util.DateUtil.parseTime;
import static java.lang.Double.parseDouble;

import com.skthvl.storefinder.entity.Address;
import com.skthvl.storefinder.entity.City;
import com.skthvl.storefinder.entity.Store;
import com.skthvl.storefinder.entity.StoreLocationType;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.repository.AddressRepository;
import com.skthvl.storefinder.repository.CityRepository;
import com.skthvl.storefinder.repository.StoreLocationTypeRepository;
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
    log.debug("address is {}", address);
    final var city = getCity(storeDto);
    log.debug("city is {}", city);
    final var locType = getLocType(storeDto);
    log.debug("location type is {}", locType);

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
