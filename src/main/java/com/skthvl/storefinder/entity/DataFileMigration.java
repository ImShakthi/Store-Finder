package com.skthvl.storefinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class representing a data file migration record.
 * Tracks files that have been processed during data migration,
 * storing their paths and checksums to prevent duplicate processing.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DataFileMigration extends Auditable {
  /**
   * Unique identifier for the migration record.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;
  
  /**
   * Path to the migrated file. Must be unique to prevent duplicate processing.
   */
  @Column(nullable = false, unique = true)
  private String filePath;
  
  /**
   * Checksum of the processed file to verify data integrity.
   */
  @Column(name = "file_checksum", nullable = false)
  private String fileChecksum;
}
