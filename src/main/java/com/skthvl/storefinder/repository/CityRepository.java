package com.skthvl.storefinder.repository;

import com.skthvl.storefinder.entity.City;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, BigInteger> {}
