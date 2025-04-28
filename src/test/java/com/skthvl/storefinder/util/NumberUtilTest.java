package com.skthvl.storefinder.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.skthvl.storefinder.exception.type.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberUtilTest {

  /**
   * Tests for the `parseRadiusToMeters` method in the `NumberUtil` class. This method converts a
   * string representing a radius (e.g., "1km", "500m") into its equivalent in meters. It throws an
   * `InvalidInputException` if the input format is invalid or the value is non-positive.
   */
  @Test
  void testParseRadiusToMeters_ValidKilometers() {
    final String input = "1km";

    final int result = NumberUtil.parseRadiusToMeters(input);

    assertEquals(1000, result);
  }

  @Test
  void testParseRadiusToMeters_ValidMeters() {
    final String input = "500m";

    final int result = NumberUtil.parseRadiusToMeters(input);

    assertEquals(500, result);
  }

  @Test
  void testParseRadiusToMeters_ZeroRadius() {
    final String input = "0";

    final int result = NumberUtil.parseRadiusToMeters(input);

    assertEquals(0, result);
  }

  @Test
  void testParseRadiusToMeters_InvalidFormat_NoUnit() {
    // Input without a proper measurement unit
    final String input = "100";

    // Expected to throw InvalidInputException
    Assertions.assertThrows(
        InvalidInputException.class, () -> NumberUtil.parseRadiusToMeters(input));
  }

  @Test
  void testParseRadiusToMeters_InvalidFormat_InvalidUnit() {
    // Input with an invalid unit
    final String input = "100kg";

    // Expected to throw InvalidInputException
    Assertions.assertThrows(
        InvalidInputException.class, () -> NumberUtil.parseRadiusToMeters(input));
  }

  @Test
  void testParseRadiusToMeters_InvalidFormat_NonNumericValue() {
    // Input with non-numeric value
    final String input = "abc";

    // Expected to throw InvalidInputException
    Assertions.assertThrows(
        InvalidInputException.class, () -> NumberUtil.parseRadiusToMeters(input));
  }

  @Test
  void testParseRadiusToMeters_InvalidFormat_NegativeMeters() {
    // Input with negative meter value
    final String input = "-500m";

    // Expected to throw InvalidInputException
    Assertions.assertThrows(
        InvalidInputException.class, () -> NumberUtil.parseRadiusToMeters(input));
  }

  @Test
  void testParseRadiusToMeters_ValidInputWithSpaces() {
    // Input with spaces
    final String input = "  2km ";

    // Expected to return 2000 meters
    final int result = NumberUtil.parseRadiusToMeters(input);

    assertEquals(2000, result);
  }

  @Test
  void testParseRadiusToMeters_ValidMixedCase() {
    // Mixed case input (e.g., "Km" instead of "km")
    final String input = "3Km";

    // Expected to return 3000 meters
    final int result = NumberUtil.parseRadiusToMeters(input);

    assertEquals(3000, result);
  }
}
