package com.skthvl.storefinder.repository;

import com.skthvl.storefinder.entity.City;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link City} entities in the database. Provides standard JPA
 * operations for City entities using BigInteger as the ID type. This repository is used for CRUD
 * operations on the city table.
 */
@Repository
public interface CityRepository extends JpaRepository<City, BigInteger> {}
