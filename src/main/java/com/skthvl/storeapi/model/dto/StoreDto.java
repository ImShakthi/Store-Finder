package com.skthvl.storeapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.skthvl.storeapi.config.jackson.SafeLocalTimeDeserializer;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object representing store information for the Store Api application. Contains
 * store location, identification, and operational details.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StoreDto implements Serializable {
  /** Unique identifier for the store. */
  @JsonProperty("uuid")
  private String storeId;

  /** SAP system store identifier. */
  @JsonProperty("sapStoreID")
  private String sapStoreId;

  /** Store's geographical longitude. */
  private double longitude;

  /** Store's geographical latitude. */
  private double latitude;

  /** Complex number identifier. */
  private String complexNumber;

  /** Flag indicating if warning message should be displayed. */
  private boolean showWarningMessage;

  /** Flag indicating if store is a collection point. */
  private boolean collectionPoint;

  /** Store opening time for current day. */
  @JsonDeserialize(using = SafeLocalTimeDeserializer.class)
  private LocalTime todayOpen;

  /** Store closing time for current day. */
  @JsonDeserialize(using = SafeLocalTimeDeserializer.class)
  private LocalTime todayClose;

  /** Type of store location. */
  private String locationType;

  /** City where store is located. */
  private String city;

  /** Primary street address. */
  private String street;

  /** Secondary street address. */
  private String street2;

  /** Additional street address. */
  private String street3;

  /** Display name for the store address. */
  private String addressName;

  /** Postal code of store location. */
  private String postalCode;
}
