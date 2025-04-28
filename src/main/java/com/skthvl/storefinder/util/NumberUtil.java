package com.skthvl.storefinder.util;

import com.skthvl.storefinder.exception.type.InvalidInputException;

/**
 * Utility class for number-related operations, including parsing and converting numeric formats.
 * Provides methods to handle operations like converting string representations of distances into
 * standardized integers.
 */
public class NumberUtil {

  /**
   * Parses a radius string representation (e.g., "800m", "1km") into an integer representing
   * meters. The string can include either "m" for meters or "km" for kilometers. A value of "0"
   * will return 0. Throws an exception if the input format is invalid or if the radius is not a
   * positive integer.
   *
   * @param input The string representation of the radius, such as "800m" or "1km". Must end in "m"
   *     for meters or "km" for kilometers, or be "0".
   * @return The radius in meters as an integer.
   * @throws InvalidInputException if the provided input is not in a valid format or is
   *     negative/zero where not allowed.
   */
  public static int parseRadiusToMeters(final String input) {
    if (input.equalsIgnoreCase("0")) {
      return 0;
    }
    final var radius = input.trim().toLowerCase(); // Normalize input

    if (radius.endsWith("km")) {
      final String valuePart = radius.substring(0, radius.length() - 2);
      return parsePositiveInteger(valuePart) * 1000;
    }

    if (radius.endsWith("m")) {
      final String valuePart = radius.substring(0, radius.length() - 1);
      return parsePositiveInteger(valuePart);
    }
    throw new InvalidInputException("Invalid radius format. Use like '800m' or '1km'.");
  }

  /**
   * Parses a string representation of a number and returns it as a positive integer. If the number
   * is not positive or the input is not properly formatted, an exception is thrown.
   *
   * @param value String representation of the number to be parsed. This input must represent a
   *     positive integer.
   * @return The parsed integer value if the input is valid and represents a positive number.
   * @throws InvalidInputException if the value is not a valid number or if it is zero or negative.
   */
  public static int parsePositiveInteger(final String value) {
    try {
      int result = Integer.parseInt(value);
      if (result <= 0) {
        throw new InvalidInputException("Radius must be a positive number.");
      }
      return result;
    } catch (NumberFormatException ex) {
      throw new InvalidInputException("Invalid number format for radius." + ex.getMessage());
    }
  }
}
