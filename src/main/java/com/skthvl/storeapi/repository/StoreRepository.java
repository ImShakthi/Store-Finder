package com.skthvl.storeapi.repository;

import com.skthvl.storeapi.entity.Store;
import com.skthvl.storeapi.model.dto.StoreDistanceDto;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing store entities with spatial search capabilities using PostGIS.
 * Provides methods for CRUD operations and advanced spatial queries.
 */
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
                s.location IS NOT NULL AND
                (:radiusInMeter = 0  OR
                ROUND(ST_Distance(s.location, point))::INTEGER <= :radiusInMeter)
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
      @Param("radiusInMeter") final double radiusInMeter,
      final Pageable pageable);

  /**
   * Finds stores based on filtering criteria.
   *
   * @param city filter by city name
   * @param openNow filter by store's current operating status
   * @param collectionPoint filter by collection point availability
   * @param storeLocationType filter by store location type
   * @param pageable pagination parameters
   * @return paginated list of stores matching the criteria
   */
  @Query(
      value =
          """
                  SELECT s
                  FROM Store s
                  JOIN s.address addr
                  JOIN s.city c
                  JOIN s.storeLocationType loc
                  WHERE (:cityNameParam IS NULL OR c.name = :cityNameParam)
                    AND (:openNowParam IS NULL
                      OR( 
                         (:openNowParam = TRUE 
                             AND s.todayOpen < CURRENT_TIME AND CURRENT_TIME < s.todayClose) 
                          OR(:openNowParam = FALSE 
                              AND (s.todayOpen > CURRENT_TIME OR CURRENT_TIME > s.todayClose)
                            )
                         )
                        )
                    AND (:collectionPointParam IS NULL OR s.collectionPoint = :collectionPointParam)
                    AND (:storeLocationTypeParam IS NULL OR loc.name = :storeLocationTypeParam)
                  ORDER BY s.storeId ASC
                  """,
      countQuery =
          """
                  SELECT COUNT(s)
                  FROM Store s
                  JOIN s.address addr
                  JOIN s.city c
                  JOIN s.storeLocationType loc
                  WHERE (:cityNameParam IS NULL OR c.name = :cityNameParam)
                    AND (:openNowParam IS NULL
                      OR( 
                         (:openNowParam = TRUE 
                             AND s.todayOpen <= CURRENT_TIME AND CURRENT_TIME <= s.todayClose) 
                          OR(:openNowParam = FALSE 
                              AND (s.todayOpen > CURRENT_TIME OR CURRENT_TIME > s.todayClose)
                            )
                         )
                      )
                    AND (:collectionPointParam IS NULL OR s.collectionPoint = :collectionPointParam)
                    AND (:storeLocationTypeParam IS NULL OR loc.name = :storeLocationTypeParam)
                  """)
  Page<Store> findStoreInfoBy(
      @Param("cityNameParam") String city,
      @Param("openNowParam") Boolean openNow,
      @Param("collectionPointParam") Boolean collectionPoint,
      @Param("storeLocationTypeParam") String storeLocationType,
      final Pageable pageable);

  /**
   * Deletes a store by its unique identifier.
   *
   * @param storeId the store's unique identifier
   */
  void deleteByStoreId(final String storeId);

  /**
   * Finds a store by its unique identifier.
   *
   * @param storeId the store's unique identifier
   * @return an Optional containing the store if found
   */
  Optional<Store> findByStoreId(final String storeId);

  /**
   * Checks if a store exists by its unique identifier.
   *
   * @param storeId the store's unique identifier
   * @return true if the store exists, false otherwise
   */
  boolean existsByStoreId(final String storeId);
}
