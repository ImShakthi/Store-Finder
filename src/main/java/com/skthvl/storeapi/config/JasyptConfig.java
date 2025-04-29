package com.skthvl.storeapi.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Jasypt encryption service. Provides encryption capabilities for sensitive
 * application properties.
 */
@Configuration
public class JasyptConfig {

  @Value("${store-api.env.jasypt-encryptor-password}")
  private String jasyptEncryptorPassword;

  /**
   * Creates and configures a pooled PBE string encryptor using MD5 and DES encryption.
   *
   * @return configured StringEncryptor bean for property encryption
   */
  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {

    final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(jasyptEncryptorPassword);
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");

    final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setConfig(config);

    return encryptor;
  }
}
