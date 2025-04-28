package com.skthvl.storefinder.repository;

import com.skthvl.storefinder.entity.Store;
import com.skthvl.storefinder.model.dto.StoreDistanceDto;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for managing store entities with spatial search capabilities using PostGIS. */
@Repository
public interface StoreRepository extends JpaRepository<Store, BigInteger> {
  /**
   * Retrieves stores ordered by distance from specified coordinates.
   *
   * @param longitude WGS84 longitude coordinate
   * @param latitude WGS84 latitude coordinate
   * @param pageable pagination parameters
   * @return paginated list of stores with their distances
   */
  @Query(
      value =
          """
            WITH params AS (
                SELECT ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS point
            )
            SELECT
                c.name AS cityName,
                a.address_name AS addressName,
                ROUND(ST_Distance(s.location, point))::INTEGER AS distanceMeters
            FROM
                store s
            INNER JOIN address a ON a.id = s.address_id
            INNER JOIN city c ON c.id = s.city_id
            CROSS JOIN
                params
            WHERE
                s.location IS NOT NULL
            ORDER BY
                s.location <-> point
          """,
      countQuery =
          """
            SELECT COUNT(*) FROM store s
            INNER JOIN address a ON a.id = s.address_id
            INNER JOIN city c ON c.id = s.city_id
            WHERE s.location IS NOT NULL
        """,
      nativeQuery = true)
  Page<StoreDistanceDto> findNearestStoresBy(
      @Param("longitude") final double longitude,
      @Param("latitude") final double latitude,
      final Pageable pageable);

  void deleteByUuid(String uuid);

  Optional<Store> findByUuid(final String uuid);
}
