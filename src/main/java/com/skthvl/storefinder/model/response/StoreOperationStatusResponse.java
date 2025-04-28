package com.skthvl.storefinder.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response class representing the operational status and timing details of a store. Can be
 * instantiated using constructor with all arguments, no arguments, or builder pattern.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StoreOperationStatusResponse {
  /** Unique identifier of the store. */
  private String storeId;

  /** Current operational status of the store. */
  private String status;

  /** Store's opening time in string format. */
  private String openingTime;

  /** Store's closing time in string format. */
  private String closingTime;
}
