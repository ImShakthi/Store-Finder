package com.skthvl.storeapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration class that enables JPA auditing and transaction management. This configuration
 * automatically tracks entity creation and modification timestamps and manages database
 * transactions across the application.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {}
