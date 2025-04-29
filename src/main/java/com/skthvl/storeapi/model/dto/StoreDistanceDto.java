package com.skthvl.storeapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object that represents store location information with calculated distance from a
 * reference point. Used for returning nearest store search results.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StoreDistanceDto {
  /** The name of the city where the store is located. */
  private String cityName;

  /** The full address or name identifier of the store. */
  private String addressName;

  /** Distance to the store from reference point in meters. */
  private Integer distanceMeters;
}
