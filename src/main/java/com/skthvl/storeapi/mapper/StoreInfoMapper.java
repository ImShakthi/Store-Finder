package com.skthvl.storeapi.mapper;

import com.skthvl.storeapi.model.dto.StoreOperationStatusDto;
import com.skthvl.storeapi.model.response.StoreOperationStatusResponse;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting store-related DTOs to response objects. Uses MapStruct for
 * automatic implementation generation.
 */
@Mapper(componentModel = "spring")
public interface StoreInfoMapper {
  /**
   * Converts a StoreOperationStatusDto to StoreOperationStatusResponse.
   *
   * @param storeOperationStatusDto the DTO containing store operation status
   * @return the corresponding response object
   */
  StoreOperationStatusResponse toResponse(final StoreOperationStatusDto storeOperationStatusDto);
}
