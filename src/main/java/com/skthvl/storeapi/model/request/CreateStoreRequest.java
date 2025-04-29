package com.skthvl.storeapi.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Request object for creating a new store with location and address details. Generated
 * constructors: - Default no-args constructor - All-args constructor with all fields - Builder
 * pattern available via @Builder annotation
 */
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoreRequest {
  /** SAP system store identifier. */
  @NotBlank(message = "sapStoreId must not be empty")
  private String sapStoreId;

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

  /** Complex number identifier. */
  @NotBlank(message = "complexNumber must not be empty")
  private String complexNumber;

  /** Flag indicating if warning message should be displayed. */
  private boolean showWarningMessage;

  /** Flag indicating if store is a collection point. */
  private boolean collectionPoint;

  /** Store opening time for current day. */
  @NotBlank(message = "todayOpen must not be empty")
  private String todayOpen;

  /** Store closing time for current day. */
  @NotBlank(message = "todayClose must not be empty")
  private String todayClose;

  /** City where store is located. */
  @NotBlank(message = "city must not be empty")
  private String city;

  /** Type of store location. */
  @NotBlank(message = "locationType must not be empty")
  private String locationType;

  /** Primary street address. */
  @NotBlank(message = "street must not be empty")
  private String street;

  /** Secondary street address. */
  @NotBlank(message = "street2 must not be empty")
  private String street2;

  /** Additional street address. */
  private String street3;

  /** Display name for the store address. */
  @NotBlank(message = "addressName must not be empty")
  private String addressName;

  /** Postal code of store location. */
  @NotBlank(message = "postalCode must not be empty")
  private String postalCode;
}
