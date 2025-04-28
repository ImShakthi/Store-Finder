package com.skthvl.storefinder.util;

import com.skthvl.storefinder.exception.type.InvalidInputException;

public class NumberUtil {

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
