package com.skthvl.storeapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.skthvl.storeapi.config.SecurityConfig;
import com.skthvl.storeapi.mapper.StoreInfoMapper;
import com.skthvl.storeapi.model.dto.StoreDistanceDto;
import com.skthvl.storeapi.model.dto.StoreOperationStatusDto;
import com.skthvl.storeapi.model.response.StoreOperationStatusResponse;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import com.skthvl.storeapi.service.StoreInfoService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StoreInfoControllerImpl.class)
class StoreInfoControllerImplMockMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JwtTokenProvider jwtTokenProvider;
  @MockitoBean private InvalidatedTokenService invalidatedTokenService;
  @MockitoBean private StoreInfoService storeInfoService;
  @MockitoBean private StoreInfoMapper storeInfoMapper;

  @Test
  @DisplayName(
      "Given coordinates and radius, when getNearestStores is called, then return paginated stores")
  void getNearestStores_shouldReturnStores() throws Exception {
    // Given
    StoreDistanceDto dto = new StoreDistanceDto(); // add test values if needed
    PageImpl<StoreDistanceDto> page = new PageImpl<>(List.of(dto));

    when(storeInfoService.findNearestStores(4.615551, 51.778461, 1000, 0, 5)).thenReturn(page);

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/stores/nearby")
                .param("longitude", "4.615551")
                .param("latitude", "51.778461")
                .param("radius", "1km")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));
  }

  @Test
  @DisplayName("Given store ID, when getStoreOperationStatus is called, then return status")
  void getStoreOperationStatus_shouldReturnOperationStatus() throws Exception {
    // Given
    final String storeId = "store123";
    final var operationStatusDto = new StoreOperationStatusDto(storeId, "OPEN", "08:00", "20:00");
    StoreOperationStatusResponse response =
        new StoreOperationStatusResponse(storeId, "OPEN", "08:00", "20:00");
    when(storeInfoService.getOperationStatusBy(storeId)).thenReturn(operationStatusDto);
    when(storeInfoMapper.toResponse(operationStatusDto)).thenReturn(response);

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/stores/{storeId}/operation-status", storeId)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
