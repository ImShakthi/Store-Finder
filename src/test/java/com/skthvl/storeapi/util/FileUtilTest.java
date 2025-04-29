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
        "3d90c7a059c9a9ab266f578f806a04c8443f8e1e8b9c248c2465d0e1193b3024", actualChecksum);
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
