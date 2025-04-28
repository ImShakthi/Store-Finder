package com.skthvl.storefinder.entity;

import static java.util.Objects.isNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigInteger;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

/**
 * Entity representing a store location with its details including address, location type, and
 * operating hours. Extends Auditable for tracking creation and modification timestamps.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Store extends Auditable {
  /** Unique identifier for the store. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  /** City where the store is located. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id")
  private City city;

  /** Detailed address information of the store. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  /** Type of store location. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_type_id")
  private StoreLocationType storeLocationType;

  /** Unique UUID for the store. */
  @Column(nullable = false)
  private String uuid;

  /** SAP system store identifier. */
  @Column(nullable = false)
  private String sapStoreId;

  /** Store complex number. */
  @Column(nullable = false)
  private String complexNumber;

  /** Geographical coordinates of the store. */
  @Column(columnDefinition = "GEOGRAPHY(Point,4326)")
  private Point location;

  /** Flag indicating if warning message should be displayed. */
  @Column(nullable = false)
  private boolean showWarningMessage;

  /** Flag indicating if store is a collection point. */
  @Column(nullable = false)
  private boolean collectionPoint;

  /** Store opening time for current day. */
  @Column(name="today_open") private LocalTime todayOpen;

  /** Store closing time for current day. */
  @Column private LocalTime todayClose;

  /** Check if store is closed based on operating hours. */
  public boolean isStoreClosed() {
    return isNull(todayOpen) && isNull(todayClose);
  }

  public double getLongitude() {
    return location.getCoordinate().x;
  }

  public double getLatitude() {
    return location.getCoordinate().y;
  }
}
