package com.skthvl.storefinder.repository;

import com.skthvl.storefinder.entity.StoreLocationType;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreLocationTypeRepository extends JpaRepository<StoreLocationType, BigInteger> {}
