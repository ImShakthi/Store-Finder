package com.skthvl.storefinder.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Request object for geographic location coordinates. Contains validated longitude and latitude
 * values.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LocationRequest {
  /** Geographic longitude coordinate in decimal degrees. Valid range: -180.0 to 180.0. */
  @NotNull(message = "Longitude is required")
  @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
  @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
  private Double longitude;

  /** Geographic latitude coordinate in decimal degrees. Valid range: -90.0 to 90.0. */
  @NotNull(message = "Latitude is required")
  @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
  @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
  private Double latitude;
}
