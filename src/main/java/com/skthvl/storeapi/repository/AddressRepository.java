package com.skthvl.storeapi.repository;

import com.skthvl.storeapi.entity.Address;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Address entities in the database. Provides CRUD operations and
 * other data access functionality for Address records. Uses Spring Data JPA with PostgreSQL
 * geographical extensions.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, BigInteger> {}
