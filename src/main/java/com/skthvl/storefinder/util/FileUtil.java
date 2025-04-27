package com.skthvl.storefinder.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for file operations, specifically handling file checksums and hex conversions.
 */
@Slf4j
public class FileUtil {
  /**
   * Calculates checksum for a given file using specified algorithm.
   *
   * @param filePath Path to the file relative to classpath
   * @param algorithm Hash algorithm to use (e.g., "MD5", "SHA-256")
   * @return Hexadecimal string representation of the checksum, or null if error occurs
   */
  public static String calculateChecksum(final String filePath, final String algorithm) {
    try {
      final var classLoader = FileUtil.class.getClassLoader();
      Path path = Paths.get(Objects.requireNonNull(classLoader.getResource(filePath)).toURI());
      MessageDigest digest = MessageDigest.getInstance(algorithm);

      final var inputStream = Files.newInputStream(path, StandardOpenOption.READ);
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        digest.update(buffer, 0, bytesRead);
      }
      byte[] checksumBytes = digest.digest();
      return bytesToHex(checksumBytes).trim();
    } catch (Exception e) {
      log.error("Error getting checksum for file: {}", filePath, e);
    }
    return null;
  }

  /**
   * Converts byte array to hexadecimal string representation.
   *
   * @param bytes Byte array to convert
   * @return Hexadecimal string representation of the bytes
   */
  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexBuilder = new StringBuilder();
    for (byte b : bytes) {
      hexBuilder.append(String.format("%02x", b));
    }
    return hexBuilder.toString();
  }
}
