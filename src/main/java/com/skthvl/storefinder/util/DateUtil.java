package com.skthvl.storefinder.util;

import com.skthvl.storefinder.exception.InvalidTimeFormatException;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Utility class for handling date and time operations in the Store Finder application. Provides
 * methods for parsing and converting time formats.
 */
public class DateUtil {
  private static final String GESLOTEN = "GESLOTEN";

  /**
   * Parses a time string in "HH:mm" format or "GESLOTEN" into LocalTime.
   *
   * @param time String representing time in "HH:mm" format or "GESLOTEN"
   * @return LocalTime object representing the parsed time, or null if input is "GESLOTEN"
   * @throws InvalidTimeFormatException if the time format is invalid
   */
  public static LocalTime parseTime(final String time) {
    if (GESLOTEN.equalsIgnoreCase(time)) {
      return null;
    }
    final var hourAndMinutes = Arrays.stream(time.split(":")).map(Integer::parseInt).toList();
    if (hourAndMinutes.size() != 2) {
      throw new InvalidTimeFormatException();
    }
    return LocalTime.of(hourAndMinutes.getFirst(), hourAndMinutes.getLast());
  }
}
