package com.skthvl.storefinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * Entity representing a physical address in the store management system.
 * Extends Auditable to track creation and modification timestamps.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Address extends Auditable {
  /** Unique identifier for the address. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;
  
  /** Postal code of the address. */
  @Column(nullable = false)
  private String postalCode;
  
  /** Name or identifier of the address location. */
  @Column(nullable = false)
  private String addressName;
  
  /** Primary street name. */
  @Column(nullable = false)
  private String street;
  
  /** House number or secondary street information. */
  @Column(nullable = false)
  private String street2;
  
  /** Additional address information (optional). */
  @Column private String street3;
}
