package com.skthvl.storefinder.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StoreDto implements Serializable {
  private String city;
  private String street;
  private String street2;
  private String street3;
  private String addressName;
  private String uuid;
  private String longitude;
  private String latitude;
  private String complexNumber;
  private boolean showWarningMessage;
  private String todayOpen;
  private String locationType;
  private boolean collectionPoint;
  private String sapStoreID;
  private String todayClose;
  private String postalCode;
}
