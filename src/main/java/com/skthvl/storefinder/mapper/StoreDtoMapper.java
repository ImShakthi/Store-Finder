package com.skthvl.storefinder.mapper;

import com.skthvl.storefinder.entity.Store;
import com.skthvl.storefinder.model.dto.StoreDto;
import com.skthvl.storefinder.model.request.CreateStoreRequest;
import com.skthvl.storefinder.model.response.StoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreDtoMapper {
  //    @Mapping(target = "longitude",source = "")
  //    @Mapping(target = "latitude",source = "")
  @Mapping(target = "locationType", source = "store.storeLocationType.name")
  @Mapping(target = "city", source = "store.city.name")
  @Mapping(target = "street", source = "store.address.street")
  @Mapping(target = "street2", source = "store.address.street2")
  @Mapping(target = "street3", source = "store.address.street3")
  @Mapping(target = "addressName", source = "store.address.addressName")
  @Mapping(target = "postalCode", source = "store.address.postalCode")
  StoreDto storeDto(final Store store);

  @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString())")
  StoreDto storeDto(final CreateStoreRequest request);

  StoreResponse toStoreResponse(final StoreDto storeDto);
}
