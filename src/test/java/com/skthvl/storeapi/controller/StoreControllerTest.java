package com.skthvl.storeapi.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.storeapi.config.SecurityConfig;
import com.skthvl.storeapi.mapper.StoreDtoMapper;
import com.skthvl.storeapi.model.dto.StoreDto;
import com.skthvl.storeapi.model.dto.StoreFilterCriteria;
import com.skthvl.storeapi.model.request.CreateStoreRequest;
import com.skthvl.storeapi.model.response.StoreResponse;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import com.skthvl.storeapi.service.StoreInfoService;
import com.skthvl.storeapi.service.StoreService;
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
@WebMvcTest(StoreController.class)
class StoreControllerMockMvcTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private JwtTokenProvider jwtTokenProvider;
  @MockitoBean private InvalidatedTokenService invalidatedTokenService;
  @MockitoBean private StoreService storeService;
  @MockitoBean private StoreDtoMapper storeDtoMapper;
  @MockitoBean private StoreInfoService storeInfoService;

  @Test
  @DisplayName("Given valid request, when createStore is called, then return created store")
  void createStore_returnsCreatedStore() throws Exception {
    // Given
    final CreateStoreRequest request =
        CreateStoreRequest.builder()
            .sapStoreId("1234")
            .complexNumber("C-100")
            .longitude(4.88456)
            .latitude(52.3676)
            .showWarningMessage(true)
            .collectionPoint(true)
            .todayOpen("08:00")
            .todayClose("20:00")
            .city("Amsterdam")
            .addressName("Jumbo Amsterdam")
            .street("Kalverstraat")
            .street2("Block B")
            .street3("Floor 1")
            .postalCode("1012NX")
            .locationType("Supermarkt")
            .build();
    StoreDto mockDto = new StoreDto();
    StoreResponse mockResponse = new StoreResponse();

    when(storeDtoMapper.storeDto(request)).thenReturn(mockDto);
    when(storeService.registerStore(mockDto)).thenReturn(mockDto);
    when(storeDtoMapper.toStoreResponse(mockDto)).thenReturn(mockResponse);

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Given filters, when getStores is called, then return paginated list")
  void getStores_returnsPaginatedResponse() throws Exception {
    // Given
    StoreDto mockDto = new StoreDto();
    StoreResponse mockResponse = new StoreResponse();
    PageImpl<StoreDto> storeDtoPage = new PageImpl<>(List.of(mockDto));

    when(storeInfoService.getStoreInfoBy(any(StoreFilterCriteria.class))).thenReturn(storeDtoPage);
    when(storeDtoMapper.toStoreResponse(mockDto)).thenReturn(mockResponse);

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/stores")
                .queryParam("city", "Amsterdam")
                .queryParam("page", "0")
                .queryParam("size", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));
  }

  @Test
  @DisplayName("Given storeId, when getStore is called, then return store")
  void getStore_returnsStore() throws Exception {
    // Given
    String storeId = "abc123";
    StoreDto mockDto = new StoreDto();
    StoreResponse mockResponse = new StoreResponse();

    when(storeService.getStore(storeId)).thenReturn(mockDto);
    when(storeDtoMapper.toStoreResponse(mockDto)).thenReturn(mockResponse);

    // When & Then
    mockMvc
        .perform(get("/api/v1/stores/{storeId}", storeId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Given storeId and request, when modifyStore is called, then return updated store")
  void modifyStore_returnsModifiedStore() throws Exception {
    // Given
    final CreateStoreRequest request =
        CreateStoreRequest.builder()
            .sapStoreId("1234")
            .complexNumber("C-100")
            .longitude(4.88456)
            .latitude(52.3676)
            .showWarningMessage(true)
            .collectionPoint(true)
            .todayOpen("08:00")
            .todayClose("20:00")
            .city("Amsterdam")
            .addressName("Jumbo Amsterdam")
            .street("Kalverstraat")
            .street2("Block B")
            .street3("Floor 1")
            .postalCode("1012NX")
            .locationType("Supermarkt")
            .build();
    String storeId = "abc123";
    StoreDto mockDto = new StoreDto();
    StoreResponse mockResponse = new StoreResponse();

    when(storeDtoMapper.storeDto(request)).thenReturn(mockDto);
    when(storeService.modifyStore(mockDto, storeId)).thenReturn(mockDto);
    when(storeDtoMapper.toStoreResponse(mockDto)).thenReturn(mockResponse);

    // When & Then
    mockMvc
        .perform(
            put("/api/v1/stores/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Given storeId, when deleteStore is called, then return success message")
  void deleteStore_returnsConfirmationMessage() throws Exception {
    // Given
    String storeId = "abc123";
    doNothing().when(storeService).deleteStore(storeId);

    // When & Then
    mockMvc
        .perform(delete("/api/v1/stores/{storeId}", storeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("store details is deleted."));
  }
}
