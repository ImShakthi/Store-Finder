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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Store extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id")
  private City city;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_type_id")
  private StoreLocationType storeLocationType;

  @Column(nullable = false)
  private String uuid;

  @Column(nullable = false)
  private String sapStoreId;

  @Column(nullable = false)
  private String complexNumber;

  @Column(columnDefinition = "GEOGRAPHY(Point,4326)")
  private Point location;

  @Column(nullable = false)
  private boolean showWarningMessage;

  @Column(nullable = false)
  private boolean collectionPoint;

  @Column private LocalTime todayOpen;

  @Column private LocalTime todayClose;

  private boolean isStoreClosed() {
    return isNull(todayOpen) && isNull(todayClose);
  }
}
