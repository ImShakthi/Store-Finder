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
 * Entity representing different types of store locations in the system.
 * Used to categorize stores based on their type.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class StoreLocationType {
  /**
   * Unique identifier for the store location type.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;
  
  /**
   * Name of the store location type.
   */
  @Column(nullable = false)
  private String name;
  
  /**
   * Optional description providing additional details about the store location type.
   */
  @Column private String description;
}
