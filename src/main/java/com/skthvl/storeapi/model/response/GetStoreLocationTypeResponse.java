package com.skthvl.storeapi.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** GetStoreLocationTypeResponse represents the list of location type of stores. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreLocationTypeResponse {
  private List<String> types;
}
