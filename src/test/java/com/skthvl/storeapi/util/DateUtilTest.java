package com.skthvl.storeapi.util;

import static org.junit.jupiter.api.Assertions.*;

import com.skthvl.storeapi.exception.type.InvalidTimeFormatException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class DateUtilTest {
  @Test
  void parseTime_validTime_returnsLocalTime() {
    LocalTime result = DateUtil.parseTime("14:30");
    assertNotNull(result);
    assertEquals(LocalTime.of(14, 30), result);
  }

  @Test
  void parseTime_gesloten_returnsNull() {
    LocalTime result = DateUtil.parseTime("GESLOTEN");
    assertNull(result);
  }

  @Test
  void parseTime_invalidTimeFormat_throwsException() {
    assertThrows(InvalidTimeFormatException.class, () -> DateUtil.parseTime("1430"));
  }

  @Test
  void parseTime_invalidNumericValues_throwsException() {
    assertThrows(NumberFormatException.class, () -> DateUtil.parseTime("14:xx"));
  }
}
