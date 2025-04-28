package com.skthvl.storefinder.mapper;

import com.skthvl.storefinder.model.dto.StoreOperationStatusDto;
import com.skthvl.storefinder.model.response.StoreOperationStatusResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreInfoMapper {
  StoreOperationStatusResponse toResponse(final StoreOperationStatusDto storeOperationStatusDto);
}
