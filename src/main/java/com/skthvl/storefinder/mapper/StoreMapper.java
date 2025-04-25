package com.skthvl.storefinder.mapper;

import com.skthvl.storefinder.entity.Store;
import com.skthvl.storefinder.model.dto.StoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {

  @Mapping(target = "city", source = "city")
  @Mapping(target = "street", source = "street")
  @Mapping(target = "street2", source = "street2")
  @Mapping(target = "street3", source = "street3")
  @Mapping(target = "addressName", source = "addressName")
  @Mapping(target = "uuid", source = "uuid")
  @Mapping(target = "longitude", source = "longitude")
  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "complexNumber", source = "complexNumber")
  @Mapping(target = "showWarningMessage", source = "showWarningMessage")
  @Mapping(target = "todayOpen", source = "todayOpen")
  @Mapping(target = "locationType", source = "locationType")
  @Mapping(target = "collectionPoint", source = "collectionPoint")
  @Mapping(target = "sapStoreId", source = "sapStoreID")
  @Mapping(target = "todayClose", source = "todayClose")
  @Mapping(target = "postalCode", source = "postalCode")
  Store toStore(final StoreDto storeDto);

  @Mapping(target = "city", source = "city")
  @Mapping(target = "street", source = "street")
  @Mapping(target = "street2", source = "street2")
  @Mapping(target = "street3", source = "street3")
  @Mapping(target = "addressName", source = "addressName")
  @Mapping(target = "uuid", source = "uuid")
  @Mapping(target = "longitude", source = "longitude")
  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "complexNumber", source = "complexNumber")
  @Mapping(target = "showWarningMessage", source = "showWarningMessage")
  @Mapping(target = "todayOpen", source = "todayOpen")
  @Mapping(target = "locationType", source = "locationType")
  @Mapping(target = "collectionPoint", source = "collectionPoint")
  @Mapping(target = "sapStoreID", source = "sapStoreId")
  @Mapping(target = "todayClose", source = "todayClose")
  @Mapping(target = "postalCode", source = "postalCode")
  StoreDto toStoreDto(final Store store);
}
