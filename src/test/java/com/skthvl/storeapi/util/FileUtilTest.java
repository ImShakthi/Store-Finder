package com.skthvl.storeapi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class FileUtilTest {
  @Test
  void testCalculateChecksumValidFile() throws Exception {
    // Given
    final String filePath = "data/stores.json";

    // When
    final String algorithm = "SHA-256";
    final String actualChecksum = FileUtil.calculateChecksum(filePath, algorithm);

    // Then
    assertEquals(
        "69694cb4511f27e27ea18a80b2c5930b6d80912b63bf1fc3cc5de07465e48010", actualChecksum);
  }

  @Test
  void testCalculateChecksumInvalidFile() {
    // When
    final String actualChecksum = FileUtil.calculateChecksum("nonExistentFile.txt", "SHA-256");

    // Then
    assertNull(actualChecksum);
  }

  @Test
  void testCalculateChecksumInvalidAlgorithm() {
    // When
    final String actualChecksum = FileUtil.calculateChecksum("testFile.txt", "INVALID");

    // Then
    assertNull(actualChecksum);
  }
}
