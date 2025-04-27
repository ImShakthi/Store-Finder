package com.skthvl.storefinder.util;

import com.skthvl.storefinder.exception.InvalidTimeFormatException;
import java.time.LocalTime;
import java.util.Arrays;

public class DateUtil {
  private static final String GESLOTEN = "GESLOTEN";

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
