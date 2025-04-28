package com.skthvl.storefinder.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StoreOperationStatusDto {
  private String storeId;
  private String status;
  private String openingTime;
  private String closingTime;
}
