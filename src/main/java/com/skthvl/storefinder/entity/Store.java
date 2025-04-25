package com.skthvl.storefinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigInteger;
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
public class Store extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String street;

  @Column(nullable = false)
  private String street2;

  @Column(nullable = false)
  private String street3;

  @Column(nullable = false)
  private String addressName;

  @Column(nullable = false)
  private String uuid;

  @Column(nullable = false)
  private String longitude;

  @Column(nullable = false)
  private String latitude;

  @Column(nullable = false)
  private String complexNumber;

  @Column(nullable = false)
  private boolean showWarningMessage;

  @Column(nullable = false)
  private String todayOpen;

  @Column(nullable = false)
  private String locationType;

  @Column(nullable = false)
  private String collectionPoint;

  @Column(nullable = false)
  private String sapStoreID;

  @Column(nullable = false)
  private String todayClose;
}
