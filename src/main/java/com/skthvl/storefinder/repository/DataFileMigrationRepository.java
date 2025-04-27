package com.skthvl.storefinder.repository;

import com.skthvl.storefinder.entity.DataFileMigration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing data file migration records. Provides operations to track and
 * verify file migrations using JPA.
 */
@Repository
public interface DataFileMigrationRepository extends JpaRepository<DataFileMigration, Integer> {
  /**
   * Checks if a file migration record exists with the given file path and checksum.
   *
   * @param filePath The path of the migrated file
   * @param fileChecksum The checksum of the migrated file
   * @return true if a matching record exists, false otherwise
   */
  boolean existsByFilePathAndFileChecksum(final String filePath, final String fileChecksum);
}
