package com.skthvl.storeapi.entity;

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
 * Entity representing a city in the store api system.
 * This class maps to the 'city' table in the database and stores basic city information.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class City {
  /**
   * Unique identifier for the city.
   * Auto-generated and cannot be updated once set.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;
  
  /**
   * Name of the city.
   * This field cannot be null.
   */
  @Column(nullable = false)
  private String name;
  
  /**
   * Optional description or additional information about the city.
   */
  @Column
  private String description;
}
