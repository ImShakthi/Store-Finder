package com.skthvl.storeapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.skthvl.storeapi.config.SecurityConfig;
import com.skthvl.storeapi.entity.City;
import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.repository.CityRepository;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CityController.class)
class CityControllerMockMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JwtTokenProvider jwtTokenProvider;

  @MockitoBean private InvalidatedTokenService invalidatedTokenService;

  @MockitoBean private CityRepository cityRepository;

  @Test
  void getCities_shouldReturnCityList() throws Exception {
    // Given
    final var city1 = new City();
    city1.setName("Amsterdam");
    final var city2 = new City();
    city2.setName("Rotterdam");

    final var cities = List.of(city1, city2);
    when(cityRepository.findAll()).thenReturn(cities);

    // When & Then
    mockMvc
        .perform(get("/api/v1/cities").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.noOfCities").value(2))
        .andExpect(jsonPath("$.cities[0]").value("Amsterdam"))
        .andExpect(jsonPath("$.cities[1]").value("Rotterdam"));
  }
}
