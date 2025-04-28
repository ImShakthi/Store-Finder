package com.skthvl.storefinder.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Response object for store information in the Store Finder application. */
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
  /** Unique identifier for the store. */
  private String storeId;

  /** SAP system store identifier. */
  private String sapStoreId;

  /** Store's geographical longitude. */
  private String longitude;

  /** Store's geographical latitude. */
  private String latitude;

  /** Complex number identifier. */
  private String complexNumber;

  /** Flag indicating if warning message should be displayed. */
  private boolean showWarningMessage;

  /** Flag indicating if store is a collection point. */
  private boolean collectionPoint;

  /** Store opening time for current day. */
  private String todayOpen;

  /** Store closing time for current day. */
  private String todayClose;

  /** City where store is located. */
  private String city;

  /** Type of store location. */
  private String locationType;

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
