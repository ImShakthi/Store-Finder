package com.skthvl.storefinder.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

  @Value("${store-finder.env.jasypt-encryptor-password}")
  private String jasyptEncryptorPassword;

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
