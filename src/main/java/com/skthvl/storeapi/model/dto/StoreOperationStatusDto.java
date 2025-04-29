package com.skthvl.storeapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object representing the operational status and timing details of a store. This
 * class provides information about store's current operational status and business hours.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StoreOperationStatusDto {
  /** Unique identifier of the store. */
  private String storeId;

  /** Current operational status of the store. */
  private String status;

  /** Store's opening time. */
  private String openingTime;

  /** Store's closing time. */
  private String closingTime;
}
