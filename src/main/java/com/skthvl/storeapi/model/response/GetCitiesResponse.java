package com.skthvl.storeapi.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** GetCitiesResponse represent list of cities store are present. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCitiesResponse {
  private int noOfCities;
  private List<String> cities;
}
