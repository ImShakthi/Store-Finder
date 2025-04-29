package com.skthvl.storeapi.repository;

import com.skthvl.storeapi.entity.UserAccount;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations and custom queries on {@link UserAccount}
 * entities.
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, BigInteger> {
  /**
   * Checks if a user account with the specified name exists in the repository.
   *
   * @param name the name of the user account to check for existence
   * @return true if a user account with the specified name exists, false otherwise
   */
  boolean existsByName(String name);

  /**
   * Retrieves an optional user account based on the given name.
   *
   * @param name the name of the user account to find
   * @return an Optional containing the UserAccount if it exists, otherwise an empty Optional
   */
  Optional<UserAccount> findByName(String name);
}
