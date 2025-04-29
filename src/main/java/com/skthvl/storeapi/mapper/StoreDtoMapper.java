package com.skthvl.storeapi.mapper;

import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.model.request.CreateStoreRequest;
import com.skthvl.storeapi.model.response.StoreResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Store entities, DTOs, and related objects. Implements
 * automatic mapping using MapStruct with Spring component model.
 */
@Mapper(componentModel = "spring")
public interface StoreDtoMapper {
  /**
   * Maps Store entity to StoreDto, handling nested property mappings for location, city and
   * address.
   *
   * @param store Source Store entity
   * @return Mapped StoreDto
   */
  @Mapping(target = "locationType", source = "store.storeLocationType.name")
  @Mapping(target = "city", source = "store.city.name")
  @Mapping(target = "street", source = "store.address.street")
  @Mapping(target = "street2", source = "store.address.street2")
  @Mapping(target = "street3", source = "store.address.street3")
  @Mapping(target = "addressName", source = "store.address.addressName")
  @Mapping(target = "postalCode", source = "store.address.postalCode")
  StoreDto storeDto(final Store store);

  /**
   * Creates new StoreDto from CreateStoreRequest, generating a random UUID as storeId.
   *
   * @param request Source CreateStoreRequest
   * @return New StoreDto instance
   */
  @Mapping(target = "storeId", expression = "java(java.util.UUID.randomUUID().toString())")
  StoreDto storeDto(final CreateStoreRequest request);

  /**
   * Converts StoreDto to StoreResponse.
   *
   * @param storeDto Source StoreDto
   * @return Mapped StoreResponse
   */
  StoreResponse toStoreResponse(final StoreDto storeDto);

  List<StoreResponse> toStoreResponse(final List<StoreDto> storeDtoList);
}
