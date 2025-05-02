package com.skthvl.storeapi.repository;

import com.skthvl.storeapi.entity.StoreLocationType;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link StoreLocationType} entities. Provides CRUD operations
 * for store location types using Spring Data JPA. The repository uses {@link BigInteger} as the ID
 * type for the entities.
 */
@Repository
public interface StoreLocationTypeRepository extends JpaRepository<StoreLocationType, BigInteger> {
  Optional<StoreLocationType> findByName(final String name);
}
